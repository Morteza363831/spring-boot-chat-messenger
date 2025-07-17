package com.example.messengercommand.mysql.handler;

import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengercommand.model.Session;
import com.example.messengercommand.mysql.repository.SessionRepositoryMySql;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MySqlSessionCommandHandler implements MySqlCommandHandler<Session> {

    private final SessionRepositoryMySql sessionRepositoryMySql;

    @Override
    public void handle(RequestTypes requestType, Session session) {
        switch (requestType) {
            case SAVE -> saveSession(session);
            case UPDATE -> updateSession(session);
            case DELETE -> deleteSession(session);
        }
    }


    private void saveSession(Session session) {
        sessionRepositoryMySql.save(session);
    }

    private void deleteSession(Session session) {
        sessionRepositoryMySql.delete(session);
    }

    private void updateSession(Session session) {
        Optional<Session> sessionOptional = sessionRepositoryMySql.findById(session.getId());

        if (sessionOptional.isEmpty()) {
            // TODO
            throw new RuntimeException("Session not found");
        }
        sessionRepositoryMySql.save(session);
    }
}
