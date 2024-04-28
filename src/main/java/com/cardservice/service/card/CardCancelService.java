package com.cardservice.service.card;

import com.cardservice.client.CommonServiceClient;
import com.cardservice.dto.CancelApiResponse;
import com.cardservice.dto.CancelRequestDto;
import com.cardservice.dto.KafkaSend;
import com.cardservice.dto.common.CommonApiResult;
import com.cardservice.entity.Approve;
import com.cardservice.kafka.KafkaProducer;
import com.cardservice.message.ApiResponseMessage;
import com.cardservice.repository.ApproveRepository;
import com.cardservice.service.van.VanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardCancelService {

    private final ApproveRepository approveRepository;
    private final CommonServiceClient commonServiceClient;
    private final Map<String, VanService> vanServiceMap;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public ResponseEntity<?> cancel(String transactionId, CancelRequestDto cancelRequestDto, String authorization) {

        // 취소 금액 셋팅
        long amount = 0;
        boolean isFullCancel = false;

        if(cancelRequestDto.getCancelAmount() != null) amount = cancelRequestDto.getCancelAmount();

        // 카드 원거래 내역 찾기
        Optional<Approve> approve = approveRepository.findApproveByTransactionId(transactionId);
        if(approve.isEmpty())
            throw new IllegalArgumentException(ApiResponseMessage.NOT_FOUND_APPROVE.message());

        // 취소 금액 셋팅안했을 경우 -> 전체 취소
        if(amount == 0) {
            amount = approve.get().getAmount();
            isFullCancel = true;
        }

        // 취소 금액 검증 기취소 거래 취소 금액
        if(approve.get().getAmount() < amount)
            throw new IllegalArgumentException(ApiResponseMessage.INVALID_AMOUNT.message());

        // TODO 공통 api 호출 시 승인과 동일한 api 요청으로 할 지 새로운 api 개발할지 정해야됨
        ResponseEntity<CommonApiResult> result = commonServiceClient.verificationMerchant(authorization, amount);

        // 승인 당시 pg사와 현재 터미널 pg사 비교
        if(!approve.get().getVan().equals(Objects.requireNonNull(result.getBody()).getVan()))
            throw new IllegalStateException(ApiResponseMessage.INCONSISTENCY_TERMINAL.message());

        // 3. 취소 api 연동
        VanService vanService = vanServiceMap.get(result.getBody().getVan());
        Map<String, Object> resultMap = vanService.cancel(approve.get(), amount, result.getBody(), isFullCancel);

        // 4. 승인 데이터 업데이트
        String resultCode = (String)resultMap.get("resultCode");
        String resultMessage = (String) resultMap.get("resultMessage");
        if ("0000".equals(resultCode)) {
            approve.get().decreaseAmount(amount);
        }
        // 5. kafka topic 이벤트 발행
        kafkaProducer.cancelSend(KafkaSend.builder()
                        .terminalId(result.getBody().getTerminalId())
                        .transactionId(approve.get().getTransactionId())
                        .amount(amount)
                        .vanResultCode(resultCode)
                        .vanResultMessage(resultMessage)
                        .build());

        return ResponseEntity.status(HttpStatus.OK).body(CancelApiResponse.builder()
                .originTransactionId(approve.get().getTransactionId())
                .resultCode(resultCode)
                .resultMessage(resultMessage)
                .build());
    }
}
