package com.cardservice.dto;

import lombok.Data;

@Data
public class CancelRequestDto {

    private String cancelReason;
    private Long cancelAmount;
}
