package com.cardservice.repository;

import com.cardservice.entity.Approve;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ApproveRepository extends JpaRepository<Approve, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Approve> findApproveByTransactionId(String transactionId);
}
