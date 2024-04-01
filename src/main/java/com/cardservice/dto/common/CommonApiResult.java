package com.cardservice.dto.common;

import lombok.Data;

@Data
public class CommonApiResult {
    private String code;
    private String message;
    private String terminalId;
    private String van;
    private String vanId;
    private String vanKey;
}
