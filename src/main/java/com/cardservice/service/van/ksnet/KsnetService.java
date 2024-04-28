package com.cardservice.service.van.ksnet;

import com.cardservice.dto.CardRequestDto;
import com.cardservice.dto.common.CommonApiResult;
import com.cardservice.entity.Approve;
import com.cardservice.service.van.VanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class KsnetService implements VanService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ksnet.old-keyIn-url}")
    String oldKeyInUrl;
    @Value("${ksnet.non-keyIn-url}")
    String nonKeyInUrl;
    @Value("${ksnet.cancel-url}")
    String cancelUrl;

    public KsnetService(RestTemplate restTemplate, ObjectMapper objectMapper){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    @Override
    public Map<String, Object> approveCardKeyIn(String transactionId,
                                                CardRequestDto cardRequestDto,
                                                CommonApiResult commonApiResult, String method) {

        Map<String, Object> resultMap = new HashMap<>();
        KsnetResponse ksnetResponse = callKsnetApi(transactionId, cardRequestDto, commonApiResult, method);

        log.info("API RESULT : {}", ksnetResponse);


        String resultCode = ksnetResponse.getData().getRespCode();
        String resultMessage = ksnetResponse.getData().getRespMessage();
        // 정상 승인일 경우
        if ("0000".equals(resultCode)) {
            resultMap.put("issuerCardType", ksnetResponse.getData().getIssuerCardType());
            resultMap.put("issuerCardName", ksnetResponse.getData().getIssuerCardName());
            resultMap.put("purchaseCardType", ksnetResponse.getData().getPurchaseCardType());
            resultMap.put("purchaseCardName", ksnetResponse.getData().getPurchaseCardName());
            resultMap.put("cardType", ksnetResponse.getData().getCardType());
            resultMap.put("approvalNumber", ksnetResponse.getData().getApprovalNumb());
            resultMap.put("tradeDateTime", ksnetResponse.getData().getTradeDateTime());
            resultMap.put("installment", ksnetResponse.getData().getInstallMonth());
            resultMap.put("expiryDate", ksnetResponse.getData().getExpiryDate());
            resultMap.put("cardNumber", ksnetResponse.getData().getCardNumb());
            resultMap.put("vanTrxId", ksnetResponse.getData().getTid());
        }
        resultMap.put("resultCode", resultCode);
        resultMap.put("resultMessage", resultMessage);

        return resultMap;
    }

    @Override
    public Map<String, Object> cancel(Approve approve, Long amount, CommonApiResult commonApiResult, boolean isFullCancel) {
        KsnetResponse ksnetResponse = callCancelApi(approve, amount, commonApiResult, isFullCancel);
        log.info("API RESULT : {}", ksnetResponse);

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("resultCode", ksnetResponse.getData().getRespCode());
        resultMap.put("resultMessage", ksnetResponse.getData().getRespMessage());

        return resultMap;
    }

    private KsnetResponse callCancelApi(Approve approve, Long amount, CommonApiResult commonApiResult, boolean isFullCancel) {
        KsnetCancelRequestDto ksnetCancelRequestDto = setCancelDto(approve, amount, commonApiResult.getVanId(), isFullCancel);

        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        headers.set("Authorization", commonApiResult.getVanKey());
        HttpEntity<String> entity = null;

        try {
            entity = new HttpEntity<>(objectMapper.writeValueAsString(ksnetCancelRequestDto), headers);
        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException", e);
        }
        log.info("request => {}", Objects.requireNonNull(entity).getBody());

        ResponseEntity<KsnetResponse> response = restTemplate.postForEntity(cancelUrl, entity, KsnetResponse.class);

        log.info("response => {}", response.toString());

        if(!response.getStatusCode().is2xxSuccessful()){
            log.info("KSNET 승인 취소 API 호출 실패");
            throw new RuntimeException("서버 통신 오류 잠시 후 다시 시도해주세요.");
        }

        return response.getBody();
    }

    private KsnetCancelRequestDto setCancelDto(Approve approve, Long amount, String vanId, boolean isFullCancel) {
        // 부분 취소 셋팅
        if (!isFullCancel) {
            int cancelCount = approve.getCancelCount();
            if (cancelCount == 0) {
                cancelCount = 1;
            }else{
                cancelCount = cancelCount + 1;
            }
            return KsnetCancelRequestDto.builder()
                    .mid(approve.getVanId())
                    .cancelType("PARTIAL")
                    .orgTradeKeyType("TID")
                    .orgTradeKey(approve.getVanTrxId())
                    .cancelTotalAmount(String.valueOf(amount))
                    .cancelTaxFreeAmount("0")
                    .cancelSeq(String.valueOf(cancelCount))
                    .build();
        }
        return KsnetCancelRequestDto.builder()
                .mid(approve.getVanId())
                .cancelType("FULL")
                .orgTradeKeyType("TID")
                .orgTradeKey(approve.getVanTrxId())
                .build();
    }


    /**
     * KSNET API 요청 분기 처리
     * @param transactionId
     * @param cardRequestDto
     * @param commonApiResult
     * @param method
     * @return
     */
    private KsnetResponse callKsnetApi(String transactionId, CardRequestDto cardRequestDto, CommonApiResult commonApiResult, String method) {
        KsnetResponse ksnetResponse = null;
        if ("keyIn".equals(method)) {
            KsnetOldKeyInRequest request = setOldKeyIn(transactionId, cardRequestDto, commonApiResult);
            ksnetResponse = callOldKeyIn(request, commonApiResult.getVanKey());
        }
        return ksnetResponse;
    }



    private KsnetResponse callOldKeyIn(KsnetOldKeyInRequest request, String vanKey) {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        headers.set("Authorization", vanKey);
        HttpEntity<String> entity = null;
        try {
            entity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException", e);
        }
        log.info("request => {}", Objects.requireNonNull(entity).getBody());

        ResponseEntity<KsnetResponse> response = restTemplate.postForEntity(oldKeyInUrl, entity, KsnetResponse.class);

        log.info("response => {}", response.toString());

        if(!response.getStatusCode().is2xxSuccessful()){
            log.info("KSNET 구인증 API 호출 실패");
            throw new RuntimeException("서버 통신 오류 잠시 후 다시 시도해주세요.");
        }

        return response.getBody();
    }

    /**
     * 수기결제(카드번호, 유효기간, 생년월일, 비밀번호) REQUEST SET
     * @param transactionId
     * @param cardRequestDto
     * @param commonApiResult
     * @return
     */
    private KsnetOldKeyInRequest setOldKeyIn(String transactionId, CardRequestDto cardRequestDto, CommonApiResult commonApiResult) {
        // 할부 개월 셋팅
        String installment = "0";
        if (!cardRequestDto.getInstallment().isEmpty()) {
            installment = cardRequestDto.getInstallment();
        }
        return KsnetOldKeyInRequest.builder()
                .mid(commonApiResult.getVanId())                            // 상점 아이디(KSNET에서 부여한 ID)
                .orderNumb(transactionId)                                   // 가맹점 주문번호(transactionId)
                .userName(cardRequestDto.getOrderName())                    // 주문자명
                .productType("REAL")                                        // 상품 구분(REAL:실물상품, DIGITAL:디지털컨텐츠)
                .productName(cardRequestDto.getProductName())               // 상품명
                .totalAmount(String.valueOf(cardRequestDto.getAmount()))    // 총금액
                .taxFreeAmount("0")                                         // 면세 금액
                .interestType("PG")                                         // 이자 구분(PG:카드사, MALL:가맹점 별도지정)
                .cardNumb(cardRequestDto.getCardNumber())                   // 카드 번호
                .expiryDate(cardRequestDto.getExpireDate())                 // 유효 기간
                .installMonth(installment)                                  // 할부 개월 수
                .currencyType("KRW")                                        // 통화 타입(KRW:원화, USD:달러)
                .password2(cardRequestDto.getPassword())                    // 비밀번호
                .userInfo(cardRequestDto.getUserInfo())                     // 생년월일
                .build();
    }
}
