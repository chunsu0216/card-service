package com.cardservice.repository;

import com.cardservice.entity.CardRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {
    boolean existsByOrderId(String orderId);
    Optional<CardRequest> findByOrderId(String orderId);
}
