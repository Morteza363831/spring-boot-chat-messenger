package com.example.springbootchatmessenger.user;


import com.example.springbootchatmessenger.exceptions.CustomValidationException;
import com.example.springbootchatmessenger.exceptions.EntityAlreadyExistException;
import com.example.springbootchatmessenger.exceptions.CustomEntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * this class will handle users
 * --> save will save a user in database
 * --> getUserById will get a user using his id not his username !
 * --> deleteUserById will delete a user with his id
 * --> getAllUsers will get all users from database
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final Validator validator;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepository userRepository,
                           final PasswordEncoder passwordEncoder,
                           final Validator validator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }



    @Transactional(rollbackOn = Exception.class)
    @Override
    public UserDto save(final UserCreateDto userCreateDto) {

        validate(userCreateDto);

        if (userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new EntityAlreadyExistException(userCreateDto.getUsername());
        }

        UserEntity userEntity = UserMapper.INSTANCE.toEntity(userCreateDto);
        userEntity.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        userEntity = userRepository.save(userEntity);
        return UserMapper.INSTANCE.toUserDto(userEntity);
    }

    @Override
    public UserDto getUserById(final UUID uuid) {
        return userRepository.findById(uuid)
                .map(UserMapper.INSTANCE::toUserDto)
                .orElseThrow(() -> new CustomEntityNotFoundException(uuid.toString()));
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void updateUser(final String username, final UserUpdateDto userUpdateDto) {

        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new CustomEntityNotFoundException(username);
        }
        userRepository.save(UserMapper.INSTANCE.partialUpdate(userUpdateDto, user.get()));
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteUserById(UserDeleteDto userDeleteDto) {

        final Optional<UserEntity> user = userRepository.findByUsername(userDeleteDto.getUsername());

        if (user.isEmpty()) {
            throw new CustomEntityNotFoundException(userDeleteDto.getUsername());
        }
        userRepository.deleteById(user.get().getId());
    }

    @Override
    public List<UserDto> getAllUsers() {

        final List<UserEntity> users = userRepository.findAll();
        // Use Optional to handle the case where the list might be empty
        return Optional.of(users)
                .filter(userList -> !userList.isEmpty()) // Check if the list is not empty
                .map(UserMapper.INSTANCE::userEntityListToUserDtoList) // Map to DTOs and collect into a list
                .orElseGet(ArrayList::new); // Return an empty list if no users found
    }

    @Override
    public UserDto getUserByUsername(final String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper.INSTANCE::toUserDto)
                .orElseThrow(() -> new CustomEntityNotFoundException(username));
    }


    private void validate(Object userCreateDto) {
        List<String> violations = new ArrayList<>();
        validator.validate(userCreateDto).forEach(field -> violations.add(field.getMessage()));
        if (!violations.isEmpty()) {
            throw new CustomValidationException(violations);
        }
    }
}
