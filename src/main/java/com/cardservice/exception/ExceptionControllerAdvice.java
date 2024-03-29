package com.cardservice.exception;

import feign.FeignException;
import jakarta.ws.rs.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExceptionHandler(IllegalArgumentException e){
        log.error("[IllegalArgumentException handler]", e);

        return ErrorResult.builder()
                .errorCode("0400")
                .errorMessage(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorResult forbiddenExceptionHandler(ForbiddenException e){
        log.error("[forbiddenExceptionHandler handler]", e);

        return ErrorResult.builder()
                .errorCode("0403")
                .errorMessage(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(FeignException.Unauthorized.class)
    public ErrorResult FeignClientException(FeignException.Unauthorized e){
        log.error("[FeignClientException Unauthorized handler]", e);

        return ErrorResult.builder()
                .errorCode("401")
                .errorMessage(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(FeignException.Conflict.class)
    public ErrorResult FeignClientException(FeignException.Conflict e){
        log.error("[FeignClientException Conflict handler]", e);

        return ErrorResult.builder()
                .errorCode("409")
                .errorMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(CommonServiceException.class)
    public ErrorResult conflictExceptionHandler(CommonServiceException e){
        log.error("[CommonServiceException handler]", e);

        return ErrorResult.builder()
                .errorCode(e.getCode())
                .errorMessage(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResult internalServerException(RuntimeException e){
        log.error("[internalServerException handler]", e);

        return ErrorResult.builder()
                .errorCode("0500")
                .errorMessage(e.getMessage())
                .build();
    }
}
