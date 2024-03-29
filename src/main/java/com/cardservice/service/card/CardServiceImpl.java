package com.cardservice.service.card;

import com.cardservice.client.CommonServiceClient;
import com.cardservice.dto.CardRequestDto;
import com.cardservice.message.ApiResponseMessage;
import com.cardservice.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService{

    private final CommonServiceClient commonServiceClient;
    @Transactional
    @Override
    public Object keyIn(CardRequestDto cardRequestDto, String authorization, String method) {

        // 유효기간 검증
        if(!ValidationUtil.validExpireDate(cardRequestDto.getExpireDate())){
            throw new IllegalArgumentException(ApiResponseMessage.INVALID_EXPIRE_DATE.message());
        }

        // 구인증(카유생비)일 경우 추가 검증
        if ("keyIn".equals(method)) {
            // 생년월일 검증
            if(!ValidationUtil.validUserInfo(cardRequestDto.getUserInfo())){
                throw new IllegalArgumentException(ApiResponseMessage.INVALID_BIRTHDAY.message());
            }
            // 비밀번호 자릿수 검증
            if(!ValidationUtil.validPassword(cardRequestDto.getPassword())){
                throw new IllegalArgumentException(ApiResponseMessage.INVALID_PASSWORD.message());
            }
        }

        // 공통 서비스 호출
        // 정상이 아닐 경우 공통 서비스로부터 받은 응답 그대로 API 응답 내려주고 exception 처리
        commonServiceClient.verificationMerchant(authorization, cardRequestDto.getAmount());

        // 가맹점 별 주문 ID 중복 검증






        return null;
    }


    @Override
    public Object cancel() {
        return null;
    }
}
