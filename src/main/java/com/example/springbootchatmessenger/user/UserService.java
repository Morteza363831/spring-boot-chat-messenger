package com.example.springbootchatmessenger.user;

import java.util.List;

public interface UserService {


    UserEntityDto save(UserEntityDto userEntityDto);

    UserEntityDto getUserById(Long id);

    void deleteUserById(Long id);

    List<UserEntityDto> getAllUsers();

    UserEntityDto getUserByUsername(String username);
}
