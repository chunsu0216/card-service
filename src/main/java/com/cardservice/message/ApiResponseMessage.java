package com.cardservice.message;

public enum ApiResponseMessage {
    INVALID_EXPIRE_DATE("올바르지않은 유효기간입니다."),
    INVALID_BIRTHDAY("올바르지않은 생년월일입니다."),
    INVALID_PASSWORD("올바르지않은 비밀번호입니다.");
    private final String message;
    ApiResponseMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
