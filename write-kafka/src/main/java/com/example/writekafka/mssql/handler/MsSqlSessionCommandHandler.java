package com.example.writekafka.mssql.handler;

import com.example.messenger.utility.RequestTypes;
import com.example.writekafka.model.Session;
import com.example.writekafka.mssql.repository.SessionRepositoryMsSql;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
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
        Optional<Session> sessionOptional = sessionRepositoryMsSql.findById(session.getId());

        if (sessionOptional.isEmpty()) {
            // TODO
            throw new RuntimeException("Session not found");
        }
        sessionRepositoryMsSql.save(session);
    }
}
