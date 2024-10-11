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
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    public UserEntityDto save(UserEntityDto userEntityDto) {
        //Optional<UserEntity> userEntityOptional = getUserById();
        UserEntity userEntity = modelMapper.map(userEntityDto, UserEntity.class);
        return modelMapper.map(userRepository.save(userEntity), UserEntityDto.class);
    }

    @Override
    public UserEntityDto getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        return modelMapper.map(userEntity, UserEntityDto.class);
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
                .map(userList -> userList.stream()
                        .map(userEntity -> modelMapper.map(userEntity, UserEntityDto.class))
                        .collect(Collectors.toList())) // Map to DTOs and collect into a list
                .orElseGet(ArrayList::new); // Return an empty list if no users found
    }

    @Override
    public UserEntityDto getUserByUsername(String username) {
        return null;
    }


}
