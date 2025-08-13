package com.example.messenger.user.service;


import com.example.messenger.aop.AfterThrowingException;
import com.example.messenger.exceptions.EntityAlreadyExistException;
import com.example.messenger.exceptions.EntityNotFoundException;
import com.example.messenger.kafka.CommandProducer;
import com.example.messenger.user.model.UserUpdateDto;
import com.example.messenger.user.model.*;
import com.example.messenger.user.query.UserQueryClient;
import com.example.messenger.utility.AuthorityUpdateType;
import com.example.messenger.utility.Validator;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengerutilities.utility.TopicNames;
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
@AfterThrowingException
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
                .orElseThrow(() -> new EntityNotFoundException(username));
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
                .orElseThrow(() -> new EntityNotFoundException(username)); // may change
    }

    @Override
    public UserDto save(UserCreateDto userCreateDto) {

        validator.validate(userCreateDto);

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

    @Override
    public void update(String username, UserUpdateDto userUpdateDto) {

        validator.validate(userUpdateDto);

        userQueryClient.getUser(username)
                .ifPresentOrElse(foundedUser -> {
                    // produce event on topic (command request)
                    commandProducer.sendWriteEvent(TopicNames.USER_WRITE_TOPIC,
                            RequestTypes.UPDATE,
                            DataTypes.USER, userMapper.partialUpdate(userUpdateDto, foundedUser));
                }, () -> {
                    throw new EntityNotFoundException(username);
                });
    }

    @Override
    public void updateUserAuthorities(UserEntity userEntity, String authority, AuthorityUpdateType type) {
        final UserUpdateDto userUpdateDto = userMapper.userEntityToUpdateDto(userEntity);

        String updatedAuthorities;
        if (AuthorityUpdateType.UPDATE.equals(type)) {
            // add authority
            updatedAuthorities = userEntity.getAuthorities() + "," + authority;
        }
        else {
            // Delete authority
            List<String> authorities = new ArrayList<>(Arrays.stream(userEntity.getAuthorities().split(",")).toList());
            authorities.removeIf(item -> item.equals(authority));
            updatedAuthorities = String.join(",", authorities);
        }

        userUpdateDto.setAuthorities(updatedAuthorities);
        // produce event on topic (command request)
        update(userEntity.getUsername(), userUpdateDto);
    }

    @Override
    public void delete(UserDeleteDto userDeleteDto) {

        validator.validate(userDeleteDto);

        userQueryClient.getUser(userDeleteDto.getUsername())
                .ifPresent(foundedUser -> {
                    // produce event on topic (command request)
                    commandProducer.sendWriteEvent(TopicNames.USER_WRITE_TOPIC, RequestTypes.DELETE, DataTypes.USER, foundedUser);
                });
    }

}
