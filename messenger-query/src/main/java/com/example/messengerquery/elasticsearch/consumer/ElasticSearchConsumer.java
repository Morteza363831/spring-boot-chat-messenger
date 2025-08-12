package com.example.messengerquery.elasticsearch.consumer;

import com.example.messengerutilities.model.SyncEventTemplate;
import com.example.messengerutilities.utility.TopicNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@EnableKafka
@Component
@RequiredArgsConstructor
public class ElasticSearchConsumer {

    private final SyncEventDispatcher syncEventDispatcher;

    @KafkaListener(topics = {
            TopicNames.USER_SYNC_TOPIC,
            TopicNames.SESSION_SYNC_TOPIC,
            TopicNames.MESSAGE_SYNC_TOPIC
    }, groupId = "elasticsearch")
    public void consume(SyncEventTemplate syncEventTemplate) {
        try {
            syncEventDispatcher.dispatch(syncEventTemplate);
        }
        catch (Exception e) {
            // implement later
            log.error(e.getMessage());
        }
    }
}
