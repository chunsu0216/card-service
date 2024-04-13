package com.cardservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class KafkaApproveDto implements Serializable {
    private Schema schema;
    private Payload payload;
}
