package com.example.messengercommand.mysql.sync;


import com.example.messengercommand.utility.LoggingUtil;
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

    // tools
    private final LoggingUtil loggingUtil;

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
                loggingUtil.error(log, this.getClass().getName(), exception.getMessage(), null);
                return;
            }
            loggingUtil.info(log,
                    this.getClass().getName(),
                    null,
                    "Sync event sent to topic : " + topic + ", result : "+ result);
        });
    }
}
