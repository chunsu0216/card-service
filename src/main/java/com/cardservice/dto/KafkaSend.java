package com.cardservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class KafkaSend {

    private String transactionId;
    private String terminalId;
    private String orderId;
    private String orderName;
    private String productName;
    private Long amount;
    private String approvalNumber;
    private String van;
    private String vanId;
    private String vanTrxId;
    private String vanResultCode;
    private String vanResultMessage;
    private String tradeDateTime;
}
