package com.example.messenger.user.service;

import com.example.messenger.user.model.*;
import com.example.messenger.utility.AuthorityUpdateType;

import java.util.List;
import java.util.UUID;

public interface UserService {


    List<UserDto> getUsers();
    UserDto getUser(String username);
    UserDto getUser(UUID uuid);

    UserEntity getUserEntity(UUID uuid);
    UserEntity getUserEntity(String username);


    UserDto save(UserCreateDto userCreateDto);
    void update(String username,UserUpdateDto userUpdateDto);
    void updateUserAuthorities(UserEntity userEntity, String authorities, AuthorityUpdateType type);
    void delete(UserDeleteDto userDeleteDto);
}
