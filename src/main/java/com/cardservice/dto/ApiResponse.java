package com.cardservice.dto;

import com.cardservice.dto.common.Card;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private String transactionId;
    private String orderId;
    private String orderName;
    private String productName;
    private Long amount;
    private String installment;
    private String approvalNumber;
    private Card card;
}
