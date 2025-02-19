package com.example.springbootchatmessenger.user;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 * this class will handle users
 * --> save will save a user in database
 * --> getUserById will get a user using his id not his username !
 * --> deleteUserById will delete a user with his id (not usable for now)
 * --> getAllUsers will get all users from database
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepository userRepository,
                           final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Transactional(rollbackOn = Exception.class)
    @Override
    public UserEntityDto save(UserEntityDto userEntityDto) {
        final UserEntity userEntity = UserMapper.INSTANCE.userDtoToUserEntity(userEntityDto);
        userEntity.setPassword(passwordEncoder.encode(userEntityDto.getPassword()));
        return UserMapper.INSTANCE.userEntityToUserDto(userRepository.save(userEntity));
    }

    @Override
    public UserEntityDto getUserById(final UUID uuid) {
        final UserEntity userEntity = userRepository.findById(uuid).orElse(null);
        return UserMapper.INSTANCE.userEntityToUserDto(userEntity);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteUserById(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    @Override
    public List<UserEntityDto> getAllUsers() {
        final List<UserEntity> users = userRepository.findAll();

        // Use Optional to handle the case where the list might be empty
        return Optional.of(users)
                .filter(userList -> !userList.isEmpty()) // Check if the list is not empty
                .map(UserMapper.INSTANCE::userEntityListToUserDtoList) // Map to DTOs and collect into a list
                .orElseGet(ArrayList::new); // Return an empty list if no users found
    }

    @Override
    public UserEntityDto getUserByUsername(final String username) {
        return UserMapper.INSTANCE.userEntityToUserDto(userRepository.findByUsername(username));
    }


}
