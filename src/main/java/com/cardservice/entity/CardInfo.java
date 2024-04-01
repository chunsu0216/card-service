package com.cardservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "CARD_INFO")
@Getter
@Builder
public class CardInfo {

    @Id @GeneratedValue
    private Long idx;
    private String transactionId;
    private String bin;
    private String last4;
    private String expireDate;
    private String password;
    private String userInfo;

    public CardInfo() {
    }

    public CardInfo(Long idx, String transactionId, String bin, String last4, String expireDate, String password, String userInfo) {
        this.idx = idx;
        this.transactionId = transactionId;
        this.bin = bin;
        this.last4 = last4;
        this.expireDate = expireDate;
        this.password = password;
        this.userInfo = userInfo;
    }
}
