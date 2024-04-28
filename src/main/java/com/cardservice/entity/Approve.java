package com.cardservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "approve")
@Getter
public class Approve{

    @Id
    private Long idx;
    private String transactionId;
    private String terminalId;
    private String method;
    private Long amount;
    private String orderId;
    private String orderName;
    private String productName;
    private String installment;
    private String issuerCardType;
    private String issuerCardName;
    private String purchaseCardType;
    private String purchaseCardName;
    private String cardType;
    private String approvalNumber;
    private String van;
    private String vanId;
    private String vanTrxId;
    private String vanResultCode;
    private String vanResultMessage;
    private LocalDateTime tradeDateTime;
    private int cancelCount;

    public void decreaseAmount(Long cancelAmount){
        if(this.amount - cancelAmount < 0) throw new IllegalArgumentException("0원 이하로 UPDATE");
        this.amount -= cancelAmount;
        this.cancelCount++;
    }

    @Builder
    public Approve(Long idx, String transactionId, String terminalId, String method, Long amount, String orderId, String orderName, String productName, String installment, String issuerCardType, String issuerCardName, String purchaseCardType, String purchaseCardName, String cardType, String approvalNumber, String van, String vanId, String vanTrxId, String vanResultCode, String vanResultMessage, LocalDateTime tradeDateTime, int cancelCount) {
        this.idx = idx;
        this.transactionId = transactionId;
        this.terminalId = terminalId;
        this.method = method;
        this.amount = amount;
        this.orderId = orderId;
        this.orderName = orderName;
        this.productName = productName;
        this.installment = installment;
        this.issuerCardType = issuerCardType;
        this.issuerCardName = issuerCardName;
        this.purchaseCardType = purchaseCardType;
        this.purchaseCardName = purchaseCardName;
        this.cardType = cardType;
        this.approvalNumber = approvalNumber;
        this.van = van;
        this.vanId = vanId;
        this.vanTrxId = vanTrxId;
        this.vanResultCode = vanResultCode;
        this.vanResultMessage = vanResultMessage;
        this.tradeDateTime = tradeDateTime;
        this.cancelCount = cancelCount;
    }

    public Approve() {

    }
}
