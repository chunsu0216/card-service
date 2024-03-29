package com.cardservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        Reader reader = response.body().asReader(StandardCharsets.UTF_8);
        String errorResult = IOUtils.toString(reader);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        CommonServiceException error = objectMapper.readValue(errorResult, CommonServiceException.class);
        int status = response.status();

        switch (status) {
            case 400:
                break;
            case 401:
                throw new FeignException.Unauthorized(error.getMessage(),
                        response.request(),
                        response.request().body(),
                        response.headers());
            case 409:
                throw new FeignException.Conflict(error.getMessage(),
                        response.request(),
                        response.request().body(),
                        response.headers());
            default:throw new Exception(error.getMessage());
        }

        throw new FeignException.FeignClientException(status, error.getMessage(), response.request(), response.request().body(), response.headers());
    }
}
