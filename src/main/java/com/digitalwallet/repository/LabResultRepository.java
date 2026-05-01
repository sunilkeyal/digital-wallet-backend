package com.digitalwallet.repository;

import com.digitalwallet.entity.LabResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabResultRepository extends MongoRepository<LabResult, String> {
    List<LabResult> findByUserId(String userId, Sort sort);
    Page<LabResult> findByUserId(String userId, Pageable pageable);
}
