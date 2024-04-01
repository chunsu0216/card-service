package com.cardservice.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCardCommon {
    private String terminalKey;
    private Long amount;

    public RequestCardCommon() {
    }

    public RequestCardCommon(String terminalKey, Long amount) {
        this.terminalKey = terminalKey;
        this.amount = amount;
    }
}
