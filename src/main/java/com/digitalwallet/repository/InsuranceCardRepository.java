package com.digitalwallet.repository;

import com.digitalwallet.entity.InsuranceCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceCardRepository extends MongoRepository<InsuranceCard, String> {
    List<InsuranceCard> findByUserId(String userId, Sort sort);
    Page<InsuranceCard> findByUserId(String userId, Pageable pageable);
}
