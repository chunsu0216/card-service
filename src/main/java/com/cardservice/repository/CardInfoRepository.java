package com.cardservice.repository;

import com.cardservice.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {
    CardInfo findCardInfoByTransactionId(String transactionId);
}
