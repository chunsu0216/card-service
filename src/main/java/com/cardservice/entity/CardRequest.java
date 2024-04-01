package com.cardservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "REQUEST_CARD")
@Getter
@Builder
public class CardRequest {

    @Id @GeneratedValue
    private Long idx;
    private String orderId;
    private String orderName;
    private String terminalId;
    private String productName;
    private Long amount;

    public CardRequest(Long idx, String orderId, String orderName, String terminalId, String productName, Long amount) {
        this.idx = idx;
        this.orderId = orderId;
        this.orderName = orderName;
        this.terminalId = terminalId;
        this.productName = productName;
        this.amount = amount;
    }

    public CardRequest() {

    }
}
