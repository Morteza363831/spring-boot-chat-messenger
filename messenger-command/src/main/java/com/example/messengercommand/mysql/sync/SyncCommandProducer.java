package com.example.messengercommand.mysql.sync;


import com.example.messengerutilities.model.SyncEventTemplate;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.SyncEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncCommandProducer {

    private final KafkaTemplate<String, SyncEventTemplate> kafkaTemplate;

    public void sendSyncEvent(String topic, SyncEventType type, Map<DataTypes, String> ids) {
        final SyncEventTemplate syncEventTemplate = SyncEventTemplate
                .builder()
                .type(type)
                .ids(ids)
                .build();

        CompletableFuture<SendResult<String, SyncEventTemplate>> future = kafkaTemplate.send(topic, syncEventTemplate);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Error while sending sync event", exception);
                return;
            }
            log.info("Sync event sent to topic {}, result : {}", topic, result);
        });
    }
}
