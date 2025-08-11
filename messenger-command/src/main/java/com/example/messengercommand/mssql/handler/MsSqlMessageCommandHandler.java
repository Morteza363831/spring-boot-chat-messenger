package com.example.messengercommand.mssql.handler;

import com.example.messengercommand.aop.AfterThrowingException;
import com.example.messengercommand.exceptions.EntityNotFoundException;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengercommand.model.Message;
import com.example.messengercommand.mssql.repository.MessageRepositoryMsSql;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@AfterThrowingException
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
        messageRepositoryMsSql.findById(message.getId())
                .ifPresentOrElse(existing -> {
                    messageRepositoryMsSql.save(message);
                }, () -> {
                    throw new EntityNotFoundException(message.getId());
                });
    }
}
