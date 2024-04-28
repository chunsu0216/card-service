package com.cardservice.kafka;

import com.cardservice.dto.KafkaSend;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;



    @SneakyThrows
    public void send(KafkaSend kafkaSend){
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonInString = "";

        jsonInString = objectMapper.writeValueAsString(kafkaSend);

        kafkaTemplate.send("approve_result", jsonInString);
        log.info("Kafka Topic Send =====> {}", jsonInString);
    }

    @SneakyThrows
    public void cancelSend(KafkaSend kafkaSend){
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonInString = "";

        jsonInString = objectMapper.writeValueAsString(kafkaSend);

        kafkaTemplate.send("cancel_result", jsonInString);
        log.info("Kafka Topic Send =====> {}", jsonInString);
    }
}
