package com.example.springbootchatmessenger.user;

import java.util.List;
import java.util.UUID;

public interface UserService {


    UserEntityDto save(final UserEntityDto userEntityDto);

    UserEntityDto getUserById(final UUID uuid);

    void deleteUserById(final UUID uuid);

    List<UserEntityDto> getAllUsers();

    UserEntityDto getUserByUsername(final String username);
}
