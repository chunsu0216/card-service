package com.cardservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CancelApiResponse {
    private String originTransactionId;
    private String resultCode;
    private String resultMessage;
}
