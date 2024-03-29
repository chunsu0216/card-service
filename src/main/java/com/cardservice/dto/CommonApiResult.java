package com.cardservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonApiResult {
    private String code;
    private String message;
}
