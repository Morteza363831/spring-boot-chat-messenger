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
 * UserServiceImpl will implement user management logic .
 * It has functionalities for (save , update , delete , get and get all users)
 * PasswordEncoder will encode password using BCrypt algorithm
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // tools
    private final Validator validator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // services (query + command)
    private final CommandProducer commandProducer;
    private final UserQueryClient userQueryClient;


    @Override
    public List<UserDto> getUsers() {
        final List<UserEntity> users = userQueryClient.getUsers();
        return users
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUser(String username) {
        return userQueryClient
                .getUser(username)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new CustomEntityNotFoundException(username));
    }

    // Not implemented
    @Override
    public UserDto getUser(UUID uuid) {
        return null;
    }

    @Override
    public UserEntity getUserEntity(UUID uuid) {
        return null;
    }

    @Override
    public UserEntity getUserEntity(String username) {
        return userQueryClient
                .getUser(username)
                .orElseThrow(() -> new CustomEntityNotFoundException(username)); // may change
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public UserDto save(UserCreateDto userCreateDto) {

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
                            DataTypes.USER, userMapper.toEntity(userCreateDto));
                });

        // retrieve user from query service
        return userQueryClient.getUser(userCreateDto.getUsername())
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new EntityNotFoundException(userCreateDto.getUsername()));
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void update(String username, UserUpdateDto userUpdateDto) {

        validate(userUpdateDto);

        userQueryClient.getUser(username)
                .ifPresentOrElse(foundedUser -> {
                    // produce event on topic (command request)
                    commandProducer.sendWriteEvent(TopicNames.USER_WRITE_TOPIC,
                            RequestTypes.UPDATE,
                            DataTypes.USER, userMapper.partialUpdate(userUpdateDto, foundedUser));
                }, () -> {
                    throw new CustomEntityNotFoundException(username);
                });
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public void delete(UserDeleteDto userDeleteDto) {

        validate(userDeleteDto);

        userQueryClient.getUser(userDeleteDto.getUsername())
                .ifPresent(foundedUser -> {
                    // produce event on topic (command request)
                    commandProducer.sendWriteEvent(TopicNames.USER_WRITE_TOPIC, RequestTypes.DELETE, DataTypes.USER, foundedUser);
                });
    }


    // utils

    private void validate(Object userCreateDto) {
        final List<String> violations = new ArrayList<>();
        validator.validate(userCreateDto).forEach(field -> violations.add(field.getMessage()));
        if (!violations.isEmpty()) {
            throw new CustomValidationException(violations);
        }
    }
}
