package com.cardservice.service.van;

import com.cardservice.dto.CancelRequestDto;
import com.cardservice.dto.CardRequestDto;
import com.cardservice.dto.common.CommonApiResult;
import com.cardservice.entity.Approve;

import java.util.Map;

public interface VanService {
    Map<String, Object> approveCardKeyIn(String transactionId, CardRequestDto cardRequestDto, CommonApiResult commonApiResult, String method);
    Map<String, Object> cancel(Approve approve, Long amount, CommonApiResult commonApiResult, boolean isFullCancel);
}
