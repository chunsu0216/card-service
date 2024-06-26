package com.cardservice.client;

import com.cardservice.dto.common.CommonApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "common-service")
public interface CommonServiceClient {

    @GetMapping("/common-service/card/{terminalKey}/{amount}")
    ResponseEntity<CommonApiResult> verificationMerchant(@PathVariable(value = "terminalKey") String terminalKey,
                                                         @PathVariable(value = "amount") Long amount);
}
