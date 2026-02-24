package com.coffeelab.loyalty.repo;

import com.coffeelab.loyalty.model.BonusTxn;
import com.coffeelab.loyalty.model.BonusTxnType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BonusTxnRepository extends JpaRepository<BonusTxn, Long> {
    List<BonusTxn> findByClient_IdOrderByCreatedAtDesc(Long clientId);
    boolean existsByOrder_IdAndTxnType(Long orderId, BonusTxnType txnType);
}