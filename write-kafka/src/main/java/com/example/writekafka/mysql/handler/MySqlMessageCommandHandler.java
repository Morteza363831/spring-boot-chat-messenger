package com.example.writekafka.mysql.handler;

import com.example.messengerutilities.utility.RequestTypes;
import com.example.writekafka.model.Message;
import com.example.writekafka.mysql.repository.MessageRepositoryMySql;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MySqlMessageCommandHandler implements MySqlCommandHandler<Message> {

    private final MessageRepositoryMySql messageRepositoryMySql;

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
    }

    private void deleteMessage(Message message) {
        messageRepositoryMySql.delete(message);
    }

    private void updateMessage(Message message) {
        Optional<Message> messageOptional = messageRepositoryMySql.findById(message.getId());

        if (messageOptional.isEmpty()) {
            // TODO
            throw new RuntimeException("Message not found");
        }
        messageRepositoryMySql.save(message);
    }
}
