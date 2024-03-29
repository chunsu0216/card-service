package com.cardservice.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResult {

    private String errorCode;
    private String errorMessage;

}
