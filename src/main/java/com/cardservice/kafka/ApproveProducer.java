package com.cardservice.kafka;

import com.cardservice.entity.Approve;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApproveProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topic.kafka}")
    String topic;

    List<Field> fields = Arrays.asList(
            new Field("string", true, "transaction_id"),
            new Field("string", true, "method"),
            new Field("int32", true, "amount"),
            new Field("string", true, "order_id"),
            new Field("string", true, "order_name"),
            new Field("string", true, "product_name"),
            new Field("string", true, "installment"),
            new Field("string", true, "issuer_card_type"),
            new Field("string", true, "issuer_card_name"),
            new Field("string", true, "purchase_card_type"),
            new Field("string", true, "purchase_card_name"),
            new Field("string", true, "card_type"),
            new Field("string", true, "approval_number"),
            new Field("string", true, "van"),
            new Field("string", true, "van_id"),
            new Field("string", true, "van_trx_id"),
            new Field("string", true, "van_result_code"),
            new Field("string", true, "van_result_message"),
            new Field("string", true, "terminal_id")
            );
    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("approve")
            .build();

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(Approve approve){

        Payload payload = setPayload(approve);
        KafkaApproveDto kafkaApproveDto = new KafkaApproveDto(schema, payload);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(kafkaApproveDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send(topic, jsonInString);
        log.info("kafka Producer Send : {}", jsonInString);

    }

    private Payload setPayload(Approve approve){
        return Payload.builder()
                .transaction_id(approve.getTransactionId())
                .method(approve.getMethod())
                .amount(Math.toIntExact(approve.getAmount()))
                .order_id(approve.getOrderId())
                .order_name(approve.getOrderName())
                .product_name(approve.getProductName())
                .installment(approve.getInstallment())
                .issuer_card_type(approve.getIssuerCardType())
                .issuer_card_name(approve.getIssuerCardName())
                .purchase_card_type(approve.getPurchaseCardType())
                .purchase_card_name(approve.getPurchaseCardName())
                .card_type(approve.getCardType())
                .approval_number(approve.getApprovalNumber())
                .van(approve.getVan())
                .van_id(approve.getVanId())
                .van_trx_id(approve.getVanTrxId())
                .van_result_code(approve.getVanResultCode())
                .van_result_message(approve.getVanResultMessage())
                .terminal_id(approve.getTerminalId())
                .build();
    }
}
