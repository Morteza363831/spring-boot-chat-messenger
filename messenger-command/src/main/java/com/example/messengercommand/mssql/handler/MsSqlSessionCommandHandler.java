package com.example.messengercommand.mssql.handler;

import com.example.messengercommand.aop.AfterThrowingException;
import com.example.messengercommand.exceptions.EntityNotFoundException;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengercommand.model.Session;
import com.example.messengercommand.mssql.repository.SessionRepositoryMsSql;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@AfterThrowingException
public class MsSqlSessionCommandHandler implements MsSqlCommandHandler<Session> {

    private final SessionRepositoryMsSql sessionRepositoryMsSql;

    @Override
    public void handle(RequestTypes requestType, Session session) {
        switch (requestType) {
            case SAVE -> saveSession(session);
            case UPDATE -> updateSession(session);
            case DELETE -> deleteSession(session);
        }
    }


    private void saveSession(Session session) {
        sessionRepositoryMsSql.save(session);
    }

    private void deleteSession(Session session) {
        sessionRepositoryMsSql.delete(session);
    }

    private void updateSession(Session session) {
        sessionRepositoryMsSql.findById(session.getId())
                .ifPresentOrElse(existing -> {
                    sessionRepositoryMsSql.save(session);
                }, () -> {
                    throw new EntityNotFoundException(session.getId());
                });
    }
}
