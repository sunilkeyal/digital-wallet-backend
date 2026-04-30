package com.digitalwallet.repository;

import com.digitalwallet.entity.LabResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabResultRepository extends MongoRepository<LabResult, String> {
    List<LabResult> findByUserId(String userId);
    List<LabResult> findByUserIdOrderByTestDateDesc(String userId);
}
