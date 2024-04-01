package com.cardservice.service.van.ksnet;

import lombok.Data;

@Data
public class KsnetResponse {

    private String aid; // API 요청 고유값
    private String code; // API 응답 코드
    private String message; // API 응답 메세지
    private com.cardservice.service.van.ksnet.Data data;
}
