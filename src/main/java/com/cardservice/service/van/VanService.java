package com.cardservice.service.van;

import com.cardservice.dto.CardRequestDto;
import com.cardservice.dto.common.CommonApiResult;

import java.util.Map;

public interface VanService {
    Map<String, Object> approveCardKeyIn(String transactionId, CardRequestDto cardRequestDto, CommonApiResult commonApiResult, String method);
}
