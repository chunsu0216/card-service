package com.cardservice.service.card;

import com.cardservice.dto.common.Card;
import com.cardservice.entity.CardInfo;
import com.cardservice.repository.CardInfoRepository;
import com.cardservice.service.encrypt.EncryptService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardInfoService {

    private final CardInfoRepository cardInfoRepository;
    private final EncryptService encryptService;

    /**
     * 카드 정보 암호화 저장
     *
     * @param transactionId
     * @param cardNumber
     * @param expireDate
     * @param password
     * @param userInfo
     */
    @SneakyThrows
    public void saveCardInfo(String transactionId, String cardNumber, String expireDate, String password, String userInfo){
        String bin = cardNumber.substring(0, 6);
        String last4 = cardNumber.substring(cardNumber.length()-4);

        bin = encryptService.encryptAES256(bin);
        last4 = encryptService.encryptAES256(last4);
        password = encryptService.encryptAES256(password);
        userInfo = encryptService.encryptAES256(userInfo);

        cardInfoRepository.save(CardInfo.builder()
                .transactionId(transactionId)
                .bin(bin)
                .last4(last4)
                .expireDate(expireDate)
                .userInfo(userInfo)
                .password(password)
                .build());
    }

    @SneakyThrows
    public Card findByTransactionId(String transactionId) {
        CardInfo cardInfo = cardInfoRepository.findCardInfoByTransactionId(transactionId);
        String bin = cardInfo.getBin();
        String last4 = cardInfo.getLast4();
        bin = encryptService.decryptAES256(bin);
        last4 = encryptService.decryptAES256(last4);
        return Card.builder()
                .bin(bin)
                .last4(last4)
                .build();
    }
}
