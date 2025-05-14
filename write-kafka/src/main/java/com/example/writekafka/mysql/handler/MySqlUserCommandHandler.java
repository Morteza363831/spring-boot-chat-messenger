package com.example.writekafka.mysql.handler;

import com.example.messengerutilities.utility.RequestTypes;
import com.example.writekafka.model.User;
import com.example.writekafka.mysql.repository.UserRepositoryMySql;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MySqlUserCommandHandler implements MySqlCommandHandler<User> {

    private final UserRepositoryMySql userRepositoryMySql;

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
    }

    private void deleteUser(User user) {
        userRepositoryMySql.delete(user);
    }

    private void updateUser(User user) {
        Optional<User> userOptional = userRepositoryMySql.findById(user.getId());

        if (userOptional.isEmpty()) {
            // TODO
            throw new RuntimeException("User not found");
        }
        userRepositoryMySql.save(user);
    }

}
