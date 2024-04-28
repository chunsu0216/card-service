package com.cardservice.message;

public enum ApiResponseMessage {
    INVALID_EXPIRE_DATE("올바르지않은 유효기간입니다."),
    INVALID_BIRTHDAY("올바르지않은 생년월일입니다."),
    INVALID_PASSWORD("올바르지않은 비밀번호입니다."),
    DUPLICATION_ORDER_ID("중복된 주문번호입니다."),
    CONFLICT_CANCEL_AMOUNT("100원 이하는 취소할 수 없습니다."),
    NOT_FOUND_APPROVE("원거래 주문번호가 존재하지 않습니다."),
    INVALID_AMOUNT("취소 요청 금액이 원거래 승인 금액보다 큽니다."),
    INCONSISTENCY_TERMINAL("승인 당시 터미널 설정된 PG사와 현재 설정된 PG사가 일치하지 않습니다.");


    private final String message;
    ApiResponseMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
