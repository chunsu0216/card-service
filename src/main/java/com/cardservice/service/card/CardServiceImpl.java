package com.cardservice.service.card;

import com.cardservice.client.CommonServiceClient;
import com.cardservice.dto.ApiResponse;
import com.cardservice.dto.CardRequestDto;
import com.cardservice.dto.common.Card;
import com.cardservice.dto.common.CommonApiResult;
import com.cardservice.entity.Approve;
import com.cardservice.entity.ApproveRefuse;
import com.cardservice.exception.ErrorResult;
import com.cardservice.message.ApiResponseMessage;
import com.cardservice.repository.ApproveRefuseRepository;
import com.cardservice.repository.ApproveRepository;
import com.cardservice.service.van.VanService;
import com.cardservice.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService{

    private final CommonServiceClient commonServiceClient;
    private final CardRequestService cardRequestService;
    private final Map<String, VanService> vanServiceMap;
    private final CardInfoService cardInfoService;
    private final ApproveRepository approveRepository;
    private final ApproveRefuseRepository approveRefuseRepository;
    @Transactional
    @Override
    public ResponseEntity<?> keyIn(CardRequestDto cardRequestDto, String authorization, String method) {

        // 유효기간 검증
        if(!ValidationUtil.validExpireDate(cardRequestDto.getExpireDate())){
            throw new IllegalArgumentException(ApiResponseMessage.INVALID_EXPIRE_DATE.message());
        }

        // 구인증(카드번호/유효기간/생년월일/비밀번호)일 경우 추가 검증, 비인증(카드번호/유효기간)
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
        ResponseEntity<CommonApiResult> result = commonServiceClient.verificationMerchant(authorization, cardRequestDto.getAmount());

        // 가맹점 별 주문 ID 중복 검증
        if(cardRequestService.isDuplicateOrderId(cardRequestDto.getOrderId()))
            throw new IllegalArgumentException(ApiResponseMessage.DUPLICATION_ORDER_ID.message());

        // 결제 요청 정보 저장, 트랜잭션 분리
        cardRequestService.saveCardRequest(cardRequestDto, Objects.requireNonNull(result.getBody()).getTerminalId());

        // 거래고유번호 생성
        String transactionId = "T" + UUID.randomUUID().toString();

        // 상위 PG API CALL
        VanService vanService = vanServiceMap.get(result.getBody().getVan());
        Map<String, Object> resultMap = vanService.approveCardKeyIn(transactionId, cardRequestDto, result.getBody(), method);
        String resultCode = (String) resultMap.get("resultCode");
        String resultMessage = (String) resultMap.get("resultMessage");

        // 승인 불가(한도 초과, 카유생비 정보 안맞는 등)
        if (!"0000".equals(resultCode)) {
            // 승인 거절 내역 저장
            approveRefuseRepository.save(setApproveRefuse(transactionId, method, cardRequestDto, result.getBody(), resultMap));

            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResult.builder()
                    .errorCode(resultCode)
                    .errorMessage(resultMessage)
                    .build());
        }

        // 카드 정보 암호화 저장
        cardInfoService.saveCardInfo(transactionId,
                                     cardRequestDto.getCardNumber(),
                                     cardRequestDto.getExpireDate(),
                                     cardRequestDto.getPassword(),
                                     cardRequestDto.getUserInfo());

        // 카드 승인 결과 저장
        Approve approve = approveRepository.save(setApprove(transactionId,
                                                            cardRequestDto,
                                                            result.getBody(),
                                                            resultMap,
                                                            method));

        // 카드 정보 복호화 및 셋팅
        Card card = getCard(transactionId, approve);

        // TODO Kafka 연동

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .transactionId(approve.getTransactionId())
                .orderId(approve.getOrderId())
                .orderName(approve.getOrderName())
                .productName(approve.getProductName())
                .amount(approve.getAmount())
                .installment(approve.getInstallment())
                .approvalNumber(approve.getApprovalNumber())
                .card(card)
                .build());
    }

    /**
     * 승인 거절 내역 저장
     * @param transactionId
     * @param method
     * @param cardRequestDto
     * @param commonInfo
     * @param resultMap
     * @return
     */
    private ApproveRefuse setApproveRefuse(String transactionId, String method, CardRequestDto cardRequestDto, CommonApiResult commonInfo, Map<String, Object> resultMap) {
        return ApproveRefuse.builder()
                .transactionId(transactionId)
                .method(method)
                .amount(cardRequestDto.getAmount())
                .orderId(cardRequestDto.getOrderId())
                .van(commonInfo.getVan())
                .van_id(commonInfo.getVanId())
                .vanResultCode(resultMap.get("resultCode").toString())
                .vanResultMessage(resultMap.get("resultMessage").toString())
                .build();
    }

    /**
     * 카드 정보 셋팅
     * @param transactionId
     * @param approve
     * @return
     */
    private Card getCard(String transactionId, Approve approve) {
        Card card = cardInfoService.findByTransactionId(transactionId);
        card.setCardType(approve.getCardType());
        card.setIssuerCardName(approve.getIssuerCardName());
        card.setIssuerCardType(approve.getIssuerCardType());
        card.setPurchaseCardName(approve.getPurchaseCardName());
        card.setPurchaseCardType(approve.getPurchaseCardType());
        return card;
    }

    /**
     * 승인 내역 저장
     * @param transactionId
     * @param cardRequestDto
     * @param commonApiResult
     * @param resultMap
     * @param method
     * @return
     */
    private Approve setApprove(String transactionId,
                               CardRequestDto cardRequestDto,
                               CommonApiResult commonApiResult, Map<String, Object> resultMap,
                               String method) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return Approve.builder()
                .transactionId(transactionId)
                .method(method)
                .terminalId(commonApiResult.getTerminalId())
                .amount(cardRequestDto.getAmount())
                .orderId(cardRequestDto.getOrderId())
                .orderName(cardRequestDto.getOrderName())
                .productName(cardRequestDto.getProductName())
                .installment(resultMap.get("installment").toString())
                .issuerCardType(resultMap.get("issuerCardType").toString())
                .issuerCardName(resultMap.get("issuerCardName").toString())
                .purchaseCardType(resultMap.get("purchaseCardType").toString())
                .purchaseCardName(resultMap.get("purchaseCardName").toString())
                .cardType(resultMap.get("cardType").toString())
                .approvalNumber(resultMap.get("approvalNumber").toString())
                .van(commonApiResult.getVan())
                .vanId(commonApiResult.getVanId())
                .vanTrxId(resultMap.get("vanTrxId").toString())
                .vanResultCode(resultMap.get("resultCode").toString())
                .vanResultMessage(resultMap.get("resultMessage").toString())
                .tradeDateTime(LocalDateTime.parse(resultMap.get("tradeDateTime").toString(), formatter))
                .build();
    }

    @Override
    public Object cancel() {
        return null;
    }
}
