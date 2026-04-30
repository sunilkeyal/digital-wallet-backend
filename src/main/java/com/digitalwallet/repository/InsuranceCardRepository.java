package com.digitalwallet.repository;

import com.digitalwallet.entity.InsuranceCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceCardRepository extends MongoRepository<InsuranceCard, String> {
    List<InsuranceCard> findByUserId(String userId);
}
