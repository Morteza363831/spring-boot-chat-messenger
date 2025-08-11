package com.example.messengercommand.mysql.handler;

import com.example.messengercommand.aop.AfterThrowingException;
import com.example.messengercommand.exceptions.EntityNotFoundException;
import com.example.messengercommand.mysql.sync.SyncCommandProducer;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengercommand.model.User;
import com.example.messengercommand.mysql.repository.UserRepositoryMySql;
import com.example.messengerutilities.utility.SyncEventType;
import com.example.messengerutilities.utility.TopicNames;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@AfterThrowingException
public class MySqlUserCommandHandler implements MySqlCommandHandler<User> {

    private final UserRepositoryMySql userRepositoryMySql;
    private final SyncCommandProducer syncCommandProducer;

    @Override
    public void handle(RequestTypes requestType, User user) {
        switch (requestType) {
            case SAVE -> saveUser(user);
            case UPDATE -> updateUser(user);
            case DELETE ->  deleteUser(user);
        }
    }

    private void saveUser(User user) {
        userRepositoryMySql.save(user);

        Map<DataTypes, String> map = Map.of(DataTypes.USER, user.getId());
        syncCommandProducer.sendSyncEvent(TopicNames.USER_SYNC_TOPIC, SyncEventType.INSERT, Map.of(DataTypes.USER, user.getId()));
    }

    private void deleteUser(User user) {
        userRepositoryMySql.delete(user);
        syncCommandProducer.sendSyncEvent(TopicNames.USER_SYNC_TOPIC, SyncEventType.DELETE, Map.of(DataTypes.USER, user.getId()));
    }

    private void updateUser(User user) {
        userRepositoryMySql.findById(user.getId())
                        .ifPresentOrElse(existing -> {
                            userRepositoryMySql.save(user);
                            syncCommandProducer.sendSyncEvent(TopicNames.USER_SYNC_TOPIC, SyncEventType.UPDATE, Map.of(DataTypes.USER, user.getId()));
                        }, () -> {
                            throw new EntityNotFoundException(user.getUsername());
                        });
    }

}
