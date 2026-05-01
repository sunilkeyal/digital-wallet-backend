package com.digitalwallet.repository;

import com.digitalwallet.entity.Immunization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmunizationRepository extends MongoRepository<Immunization, String> {
    List<Immunization> findByUserId(String userId, Sort sort);
    Page<Immunization> findByUserId(String userId, Pageable pageable);
}
