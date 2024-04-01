package com.cardservice.service.card;

import com.cardservice.dto.CardRequestDto;
import com.cardservice.entity.CardRequest;
import com.cardservice.repository.CardRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardRequestService {
    private final CardRequestRepository cardRequestRepository;

    /**
     * 주문 ID 중복 검증
     * @param orderId
     * @return
     */
    public boolean isDuplicateOrderId(String orderId){
        return cardRequestRepository.existsByOrderId(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCardRequest(CardRequestDto cardRequestDto, String terminalId){
        cardRequestRepository.save(setCardRequest(cardRequestDto, terminalId));
    }

    private CardRequest setCardRequest(CardRequestDto cardRequestDto, String terminalId) {
        return CardRequest.builder()
                .orderId(cardRequestDto.getOrderId())
                .orderName(cardRequestDto.getOrderName())
                .terminalId(terminalId)
                .productName(cardRequestDto.getProductName())
                .amount(cardRequestDto.getAmount())
                .build();
    }
}
