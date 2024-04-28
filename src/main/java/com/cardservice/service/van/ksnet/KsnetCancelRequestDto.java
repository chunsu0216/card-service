package com.cardservice.service.van.ksnet;

import lombok.Builder;
import lombok.Data;

@Data
public class KsnetCancelRequestDto {

    private String mid;
    private String cancelType;
    private String orgTradeKeyType;
    private String orgTradeKey;
    private String cancelTotalAmount;
    private String cancelTaxFreeAmount;
    private String cancelSeq;

    public KsnetCancelRequestDto() {
    }

    @Builder
    public KsnetCancelRequestDto(String mid, String cancelType, String orgTradeKeyType, String orgTradeKey, String cancelTotalAmount, String cancelTaxFreeAmount, String cancelSeq) {
        this.mid = mid;
        this.cancelType = cancelType;
        this.orgTradeKeyType = orgTradeKeyType;
        this.orgTradeKey = orgTradeKey;
        this.cancelTotalAmount = cancelTotalAmount;
        this.cancelTaxFreeAmount = cancelTaxFreeAmount;
        this.cancelSeq = cancelSeq;
    }
}
