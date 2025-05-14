package com.example.writekafka.mssql.handler;

import com.example.messengerutilities.utility.RequestTypes;
import com.example.writekafka.model.Message;
import com.example.writekafka.mssql.repository.MessageRepositoryMsSql;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MsSqlMessageCommandHandler implements MsSqlCommandHandler<Message> {

    private final MessageRepositoryMsSql messageRepositoryMsSql;

    @Override
    public void handle(RequestTypes requestType, Message message) {
        switch (requestType) {
            case SAVE -> saveMessage(message);
            case DELETE -> deleteMessage(message);
            case UPDATE -> updateMessage(message);
        }
    }


    private void saveMessage(Message message) {
        messageRepositoryMsSql.save(message);
    }

    private void deleteMessage(Message message) {
        messageRepositoryMsSql.delete(message);
    }

    private void updateMessage(Message message) {
        Optional<Message> messageOptional = messageRepositoryMsSql.findById(message.getId());

        if (messageOptional.isEmpty()) {
            // TODO
            throw new RuntimeException("Message not found");
        }
        messageRepositoryMsSql.save(message);
    }
}
