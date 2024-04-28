package com.cardservice.kafka;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {
    private String transaction_id;
    private String method;
    private Integer amount;
    private String order_id;
    private String order_name;
    private String product_name;
    private String installment;
    private String issuer_card_type;
    private String issuer_card_name;
    private String purchase_card_type;
    private String purchase_card_name;
    private String card_type;
    private String approval_number;
    private String van;
    private String van_id;
    private String van_trx_id;
    private String van_result_code;
    private String van_result_message;
    private String terminal_id;
    private int cancel_count;
}
