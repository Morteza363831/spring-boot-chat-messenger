package com.example.springbootchatmessenger.user;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * this class will handle users
 * --> save will save a user in database
 * --> getUserById will get a user using his id not his username !
 * --> deleteUserById will delete a user with his id (not usable for now)
 * --> getAllUsers will get all users from database
 */

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserEntityDto save(UserEntityDto userEntityDto) {
        //Optional<UserEntity> userEntityOptional = getUserById();
        UserEntity userEntity = UserMapper.INSTANCE.userDtoToUserEntity(userEntityDto);
        return UserMapper.INSTANCE.userEntityToUserDto(userRepository.save(userEntity));
    }

    @Override
    public UserEntityDto getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        return UserMapper.INSTANCE.userEntityToUserDto(userEntity);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserEntityDto> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        // Use Optional to handle the case where the list might be empty
        return Optional.of(users)
                .filter(userList -> !userList.isEmpty()) // Check if the list is not empty
                .map(UserMapper.INSTANCE::userEntityListToUserDtoList) // Map to DTOs and collect into a list
                .orElseGet(ArrayList::new); // Return an empty list if no users found
    }

    @Override
    public UserEntityDto getUserByUsername(String username) {
        return null;
    }


}
