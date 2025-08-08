package com.example.messengercommand.mysql.handler;

import com.example.messengercommand.mysql.sync.SyncCommandProducer;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengercommand.model.Session;
import com.example.messengercommand.mysql.repository.SessionRepositoryMySql;
import com.example.messengerutilities.utility.SyncEventType;
import com.example.messengerutilities.utility.TopicNames;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MySqlSessionCommandHandler implements MySqlCommandHandler<Session> {

    private final SessionRepositoryMySql sessionRepositoryMySql;
    private final SyncCommandProducer syncCommandProducer;

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
        syncCommandProducer.sendSyncEvent(TopicNames.SESSION_SYNC_TOPIC, SyncEventType.INSERT, Map.of(DataTypes.SESSION, session.getId()));
    }

    private void deleteSession(Session session) {
        sessionRepositoryMySql.delete(session);
        syncCommandProducer.sendSyncEvent(TopicNames.SESSION_SYNC_TOPIC, SyncEventType.DELETE, Map.of(DataTypes.SESSION, session.getId()));
        syncCommandProducer.sendSyncEvent(TopicNames.MESSAGE_SYNC_TOPIC, SyncEventType.DELETE, Map.of(DataTypes.SESSION, session.getId()));
    }

    private void updateSession(Session session) {
        Optional<Session> sessionOptional = sessionRepositoryMySql.findById(session.getId());

        if (sessionOptional.isEmpty()) {
            // TODO
            throw new RuntimeException("Session not found");
        }
        sessionRepositoryMySql.save(session);
        syncCommandProducer.sendSyncEvent(TopicNames.SESSION_SYNC_TOPIC, SyncEventType.UPDATE, Map.of(DataTypes.SESSION, session.getId()));
    }
}
