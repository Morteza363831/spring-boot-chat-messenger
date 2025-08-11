package com.example.messengercommand.mssql.handler;

import com.example.messengercommand.aop.AfterThrowingException;
import com.example.messengercommand.exceptions.EntityNotFoundException;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengercommand.model.User;
import com.example.messengercommand.mssql.repository.UserRepositoryMsSql;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@AfterThrowingException
public class MsSqlUserCommandHandler implements MsSqlCommandHandler<User> {

    private final UserRepositoryMsSql userRepositoryMsSql;

    @Override
    public void handle(RequestTypes requestType, User user) {
        switch (requestType) {
            case SAVE -> saveUser(user);
            case UPDATE -> updateUser(user);
            case DELETE ->  deleteUser(user);
        }
    }

    private void saveUser(User user) {
        userRepositoryMsSql.save(user);
    }

    private void deleteUser(User user) {
        userRepositoryMsSql.delete(user);
    }

    private void updateUser(User user) {
        userRepositoryMsSql.findById(user.getId())
                .ifPresentOrElse(existing -> {
                    userRepositoryMsSql.save(user);
                }, () -> {
                    throw new EntityNotFoundException(user.getUsername());
                });

    }

}
