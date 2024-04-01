package com.cardservice.repository;

import com.cardservice.entity.CardRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {
    boolean existsByOrderId(String orderId);
}
