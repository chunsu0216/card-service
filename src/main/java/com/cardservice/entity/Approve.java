package com.cardservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "approve")
@Getter
@Builder
public class Approve extends BaseEntity{

    @Id @GeneratedValue
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

    public Approve(Long idx, String transactionId, String terminalId, String method, Long amount, String orderId, String orderName, String productName, String installment, String issuerCardType, String issuerCardName, String purchaseCardType, String purchaseCardName, String cardType, String approvalNumber, String van, String vanId, String vanTrxId, String vanResultCode, String vanResultMessage, LocalDateTime tradeDateTime) {
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
    }

    public Approve() {
    }
}
