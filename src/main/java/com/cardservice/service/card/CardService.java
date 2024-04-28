package com.cardservice.service.card;

import com.cardservice.dto.CancelRequestDto;
import com.cardservice.dto.CardRequestDto;

public interface CardService {

    Object keyIn(CardRequestDto cardKeyInRequestDto, String authorization, String method);
    Object cancel(String transactionId, CancelRequestDto cancelRequestDto, String authorization);
}
