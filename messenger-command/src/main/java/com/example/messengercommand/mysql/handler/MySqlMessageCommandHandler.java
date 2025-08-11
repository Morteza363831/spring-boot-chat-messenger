package com.example.messengercommand.mysql.handler;

import com.example.messengercommand.aop.AfterThrowingException;
import com.example.messengercommand.exceptions.EntityNotFoundException;
import com.example.messengercommand.mysql.sync.SyncCommandProducer;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengercommand.model.Message;
import com.example.messengercommand.mysql.repository.MessageRepositoryMySql;
import com.example.messengerutilities.utility.SyncEventType;
import com.example.messengerutilities.utility.TopicNames;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@AfterThrowingException
public class MySqlMessageCommandHandler implements MySqlCommandHandler<Message> {

    private final MessageRepositoryMySql messageRepositoryMySql;
    private final SyncCommandProducer syncCommandProducer;

    @Override
    public void handle(RequestTypes requestType, Message message) {
        switch (requestType) {
            case SAVE -> saveMessage(message);
            case DELETE -> deleteMessage(message);
            case UPDATE -> updateMessage(message);
        }
    }


    private void saveMessage(Message message) {
        messageRepositoryMySql.save(message);
        syncCommandProducer.sendSyncEvent(TopicNames.MESSAGE_SYNC_TOPIC, SyncEventType.INSERT, Map.of(DataTypes.MESSAGE, message.getId()));
    }

    private void deleteMessage(Message message) {
        messageRepositoryMySql.delete(message);
        syncCommandProducer.sendSyncEvent(TopicNames.MESSAGE_SYNC_TOPIC, SyncEventType.DELETE, Map.of(DataTypes.MESSAGE, message.getId()));
    }

    private void updateMessage(Message message) {
        messageRepositoryMySql.findById(message.getId())
                .ifPresentOrElse(existing -> {
                    messageRepositoryMySql.save(message);
                    syncCommandProducer.sendSyncEvent(TopicNames.MESSAGE_SYNC_TOPIC, SyncEventType.UPDATE, Map.of(DataTypes.MESSAGE, message.getId()));
                }, () -> {
                    throw new EntityNotFoundException(message.getId());
                });
    }
}
