package com.digitalwallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@JsonDeserialize(using = UserDto.UserDtoDeserializer.class)
public class UserDto {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String dateOfBirth;
    private Set<String> roles;
    private String createdAt;
    private String updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public void setRoles(Set<String> roles) { this.roles = roles; }
    public Set<String> getRoles() { return roles; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    static class UserDtoDeserializer extends JsonDeserializer<UserDto> {
        @Override
        public UserDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            com.fasterxml.jackson.databind.JsonNode node = p.readValueAsTree();
            UserDto dto = new UserDto();
            
            if (node.has("id")) dto.setId(node.get("id").asText());
            if (node.has("email")) dto.setEmail(node.get("email").asText());
            if (node.has("password")) dto.setPassword(node.get("password").asText());
            if (node.has("firstName")) dto.setFirstName(node.get("firstName").asText());
            if (node.has("lastName")) dto.setLastName(node.get("lastName").asText());
            if (node.has("phoneNumber")) dto.setPhoneNumber(node.get("phoneNumber").asText());
            if (node.has("dateOfBirth")) dto.setDateOfBirth(node.get("dateOfBirth").asText());
            
            // Handle roles - can be string or array
            if (node.has("roles")) {
                com.fasterxml.jackson.databind.JsonNode rolesNode = node.get("roles");
                Set<String> roles = new HashSet<>();
                if (rolesNode.isArray()) {
                    rolesNode.forEach(r -> roles.add(r.asText()));
                } else if (rolesNode.isTextual()) {
                    roles.add(rolesNode.asText());
                }
                dto.setRoles(roles);
            }
            
            if (node.has("createdAt")) dto.setCreatedAt(node.get("createdAt").asText());
            if (node.has("updatedAt")) dto.setUpdatedAt(node.get("updatedAt").asText());
            
            return dto;
        }
    }
}
