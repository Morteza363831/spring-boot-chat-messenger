package com.example.messengercommand.mysql.consumer;


import com.example.messengercommand.aop.AfterThrowingException;
import com.example.messengercommand.exceptions.KafkaException;
import com.example.messengerutilities.model.KafkaDataStructure;
import com.example.messengerutilities.utility.TopicNames;
import com.example.messengercommand.mysql.handler.MySqlCommandDispatcher;
import com.example.messengercommand.utility.ConsumerTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@EnableKafka
@Component
@RequiredArgsConstructor
@AfterThrowingException
public class MySqlConsumer {


    private final MySqlCommandDispatcher dispatcher;

    @Transactional(rollbackFor = KafkaException.class)
    @KafkaListener(topics = {
            TopicNames.USER_WRITE_TOPIC,
            TopicNames.SESSION_WRITE_TOPIC,
            TopicNames.MESSAGE_WRITE_TOPIC },
            groupId = ConsumerTypes.MYSQL_GROUP)
    public void listen(KafkaDataStructure kafkaDataStructure) {
        try {
            log.info("Received: {}", kafkaDataStructure);
            dispatcher.dispatch(kafkaDataStructure);
        }
        catch (Exception e) {
            //throw new KafkaException("Mysql consumer could not to finish event processing.");
            log.error("skipped mysql");
        }
    }

}
