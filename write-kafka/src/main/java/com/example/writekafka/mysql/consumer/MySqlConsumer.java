package com.example.writekafka.mysql.consumer;


import com.example.messengerutilities.model.KafkaDataStructure;
import com.example.messengerutilities.utility.TopicNames;
import com.example.writekafka.mysql.handler.MySqlCommandDispatcher;
import com.example.writekafka.utility.ConsumerTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@EnableKafka
@Component
@RequiredArgsConstructor
public class MySqlConsumer {


    private final MySqlCommandDispatcher dispatcher;

    @KafkaListener(topics = {
            TopicNames.USER_WRITE_TOPIC,
            TopicNames.SESSION_WRITE_TOPIC,
            TopicNames.MESSAGE_WRITE_TOPIC },
            groupId = ConsumerTypes.MYSQL_GROUP)
    public void listen(KafkaDataStructure kafkaDataStructure) {
        log.info("Received: {}", kafkaDataStructure);
        dispatcher.dispatch(kafkaDataStructure);
    }

}
