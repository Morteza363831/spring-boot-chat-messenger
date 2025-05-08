package com.example.messenger.user;

import java.util.List;
import java.util.UUID;

public interface UserService {


    UserDto save(final UserCreateDto userCreateDto);

    void updateUser(final String username, final UserUpdateDto userUpdateDto);

    UserDto getUserById(final UUID uuid);

    void deleteUserById(final UserDeleteDto userDeleteDto);

    List<UserDto> getAllUsers();

    UserDto getUserByUsername(final String username);
}
