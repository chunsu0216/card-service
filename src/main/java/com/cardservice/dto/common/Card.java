package com.cardservice.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Card {
    private String cardType;
    private String issuerCardType;
    private String issuerCardName;
    private String purchaseCardType;
    private String purchaseCardName;
    private String bin;
    private String last4;
}
