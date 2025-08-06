package com.example.messenger.user.service;


import com.example.messenger.exceptions.CustomValidationException;
import com.example.messenger.exceptions.EntityAlreadyExistException;
import com.example.messenger.exceptions.CustomEntityNotFoundException;
import com.example.messenger.kafka.CommandProducer;
import com.example.messenger.user.model.UserUpdateDto;
import com.example.messenger.user.model.*;
import com.example.messenger.user.query.UserQueryClient;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengerutilities.utility.TopicNames;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class will process on user requests like create, get, update, delete and etc
 * PasswordEncoder will encode password using BCrypt algorithm
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // inject default beans
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    // inject created beans
    // Deprecated
    // private final UserRepository userRepository;
    private final CommandProducer commandProducer;
    private final UserQueryClient userQueryClient;


    @Transactional(rollbackOn = Exception.class)
    @Override
    public UserDto save(final UserCreateDto userCreateDto) {

        validate(userCreateDto);

        // retrieve user from query service
        userQueryClient.getUser(userCreateDto.getUsername())
                .ifPresentOrElse(user -> {
                    throw new EntityAlreadyExistException(userCreateDto.getUsername());
                }, () -> {
                    userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
                    // produce event on topic (command request)
                    commandProducer.sendWriteEvent(TopicNames.USER_WRITE_TOPIC,
                            RequestTypes.SAVE,
                            DataTypes.USER, UserMapper.INSTANCE.toEntity(userCreateDto));
                });
        // retrieve user from query service
        return userQueryClient.getUser(userCreateDto.getUsername())
                .map(UserMapper.INSTANCE::toUserDto)
                .orElseThrow(() -> new EntityNotFoundException(userCreateDto.getUsername()));
    }

    // For now this method is not available
    @Override
    public UserDto getUserById(final UUID uuid) {
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void updateUser(final String username, final UserUpdateDto userUpdateDto) {

        validate(userUpdateDto);

        userQueryClient.getUser(username)
                .ifPresentOrElse(foundedUser -> {
                    // produce event on topic (command request)
                    commandProducer.sendWriteEvent(TopicNames.USER_WRITE_TOPIC,
                            RequestTypes.UPDATE,
                            DataTypes.USER, UserMapper.INSTANCE.partialUpdate(userUpdateDto, foundedUser));
                }, () -> {
                    throw new CustomEntityNotFoundException(username);
                });
        // Deprecated
        /*userRepository
                .findByUsername(username)
                .ifPresentOrElse(foundedUser -> {
                    // userRepository.save(UserMapper.INSTANCE.partialUpdate(userUpdateDto, foundedUser)); // Deprecated
                    // produce event on topic (command request)
                    commandProducer.sendWriteEvent(TopicNames.USER_WRITE_TOPIC, RequestTypes.UPDATE, DataTypes.USER, foundedUser);
                }, () -> {
                    throw new CustomEntityNotFoundException(username);
                });*/
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteUserById(UserDeleteDto userDeleteDto) {

        validate(userDeleteDto);

        userQueryClient.getUser(userDeleteDto.getUsername())
                .ifPresent(foundedUser -> {
                    // produce event on topic (command request)
                    commandProducer.sendWriteEvent(TopicNames.USER_WRITE_TOPIC, RequestTypes.DELETE, DataTypes.USER, foundedUser);
                });
        // Deprecated
        /*userRepository
                .findByUsername(userDeleteDto.getUsername())
                .ifPresentOrElse(foundedUser -> {
                    userRepository.delete(foundedUser); // Deprecated
                    // produce event on topic (command request)
                    commandProducer.sendWriteEvent(TopicNames.USER_WRITE_TOPIC, RequestTypes.DELETE, DataTypes.USER, foundedUser);
                }
                , () -> {
                    throw new CustomEntityNotFoundException(userDeleteDto.getUsername());
                });*/
    }

    @Override
    public List<UserDto> getAllUsers() {
        // Deprecated
        // final List<UserEntity> users = userRepository.findAll();
        final List<UserEntity> users = userQueryClient.getUsers();
        return UserMapper.INSTANCE.userEntityListToUserDtoList(users); // Return an empty list if no users found
    }

    @Override
    public UserDto getUserByUsername(final String username) {
        return userQueryClient.getUser(username)
                .map(UserMapper.INSTANCE::toUserDto)
                .orElseThrow(() -> new CustomEntityNotFoundException(username));

        // Deprecated
        /*return userRepository.findByUsername(username)
                .map(UserMapper.INSTANCE::toUserDto)
                .orElseThrow(() -> new CustomEntityNotFoundException(username));*/
    }


    private void validate(Object userCreateDto) {
        final List<String> violations = new ArrayList<>();
        validator.validate(userCreateDto).forEach(field -> violations.add(field.getMessage()));
        if (!violations.isEmpty()) {
            throw new CustomValidationException(violations);
        }
    }
}
