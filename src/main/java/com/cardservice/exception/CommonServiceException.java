package com.cardservice.exception;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonServiceException extends RuntimeException{

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CommonServiceException(@JsonProperty("code") String code, @JsonProperty("message") String message) {
        this.code = code;
        this.message = message;
    }

    @JsonProperty("code")
    private final String code;
    @JsonProperty("message")
    private final String message;
}
