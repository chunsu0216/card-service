package com.cardservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "approve_refuse")
@Getter
@Builder
public class ApproveRefuse {

    @Id @GeneratedValue
    private Long idx;
    private String transactionId;
    private String method;
    private Long amount;
    private String orderId;
    private String van;
    private String van_id;
    private String vanResultCode;
    private String vanResultMessage;

    public ApproveRefuse(Long idx, String transactionId, String method, Long amount, String orderId, String van, String van_id, String vanResultCode, String vanResultMessage) {
        this.idx = idx;
        this.transactionId = transactionId;
        this.method = method;
        this.amount = amount;
        this.orderId = orderId;
        this.van = van;
        this.van_id = van_id;
        this.vanResultCode = vanResultCode;
        this.vanResultMessage = vanResultMessage;
    }

    public ApproveRefuse() {

    }
}
