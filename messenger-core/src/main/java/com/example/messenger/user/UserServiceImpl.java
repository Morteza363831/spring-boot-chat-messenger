package com.example.messenger.user;


import com.example.messenger.exceptions.CustomValidationException;
import com.example.messenger.exceptions.EntityAlreadyExistException;
import com.example.messenger.exceptions.CustomEntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class will process on user requests like create, get, update, delete and etc
 * PasswordEncoder will encode password using BCrypt algorithm
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    // inject default beans
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    // inject created beans
    private final UserRepository userRepository;

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

        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        final UserEntity userEntity = userRepository.save(UserMapper.INSTANCE.toEntity(userCreateDto));
        return UserMapper.INSTANCE.toUserDto(userEntity);
    }

    // For now this method is not available
    @Override
    public UserDto getUserById(final UUID uuid) {
        /*return userRepository.findById(uuid)
                .map(UserMapper.INSTANCE::toUserDto)
                .orElseThrow(() -> new CustomEntityNotFoundException(uuid.toString()));*/
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void updateUser(final String username, final UserUpdateDto userUpdateDto) {
        validate(userUpdateDto);
        userRepository
                .findByUsername(username)
                .ifPresentOrElse(foundedUser -> {
                    userRepository.save(UserMapper.INSTANCE.partialUpdate(userUpdateDto, foundedUser));
                }, () -> {
                    throw new CustomEntityNotFoundException(username);
                });
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteUserById(UserDeleteDto userDeleteDto) {
        validate(userDeleteDto);
        userRepository
                .findByUsername(userDeleteDto.getUsername())
                .ifPresentOrElse(userRepository::delete, () -> {throw new CustomEntityNotFoundException(userDeleteDto.getUsername());});

    }

    @Override
    public List<UserDto> getAllUsers() {
        final List<UserEntity> users = userRepository.findAll();
        return UserMapper.INSTANCE.userEntityListToUserDtoList(users); // Return an empty list if no users found
    }

    @Override
    public UserDto getUserByUsername(final String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper.INSTANCE::toUserDto)
                .orElseThrow(() -> new CustomEntityNotFoundException(username));
    }


    private void validate(Object userCreateDto) {
        final List<String> violations = new ArrayList<>();
        validator.validate(userCreateDto).forEach(field -> violations.add(field.getMessage()));
        if (!violations.isEmpty()) {
            throw new CustomValidationException(violations);
        }
    }
}
