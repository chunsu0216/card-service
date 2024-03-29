package com.cardservice.service.card;

import com.cardservice.dto.CardRequestDto;

public interface CardService {

    Object keyIn(CardRequestDto cardKeyInRequestDto, String authorization, String method);
    Object cancel();
}
