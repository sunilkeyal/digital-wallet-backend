package com.digitalwallet.repository;

import com.digitalwallet.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByOauth2ProviderAndOauth2Sub(String provider, String sub);
    boolean existsByEmail(String email);
}
