package com.example.messenger.kafka;

import com.example.messengerutilities.model.KafkaDataStructure;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandProducer {

    private final KafkaDataStructureMapper kafkaDataStructureMapper;

    private final KafkaTemplate<String, KafkaDataStructure> kafkaTemplate;

    public void sendWriteEvent(String topic, RequestTypes requestType, DataTypes dataType, Object object) {

        final KafkaDataStructure kafkaDataStructure = kafkaDataStructureMapper.toKafkaDataStructure(object, requestType, dataType);

        CompletableFuture<SendResult<String, KafkaDataStructure>> future = kafkaTemplate.send(topic, kafkaDataStructure);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                // TODO
                log.error("{} did not sent", object.toString());
                return;
            }
            log.info("{} sent to topic: {}", object.toString(), topic);
        });
    }
}
