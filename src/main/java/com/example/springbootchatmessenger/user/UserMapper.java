package com.example.springbootchatmessenger.user;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // dto to entity

    UserEntity toEntity(final UserDto userDto);

    UserEntity toEntity(final UserUpdateDto userUpdateDto);

    UserEntity toEntity(final UserCreateDto userCreateDto);

    default List<UserEntity> userDtoListToUserEntityList(final List<UserDto> userDtoList) {

        //TODO

        final List<UserEntity> userEntityList = new ArrayList<>();

        userDtoList.forEach(userEntityDto -> {
            userEntityList.add(toEntity(userEntityDto));
        });
        return userEntityList;
    }


    // entity to dto
    default List<UserDto> userEntityListToUserDtoList(final List<UserEntity> userEntityList) {

        //TODO

        final List<UserDto> userDtoList = new ArrayList<>();

        userEntityList.forEach(userEntity -> {
            userDtoList.add(toUserDto(userEntity));
        });
        return userDtoList;
    }

    @Mapping(target = "authorities", expression = "java(com.example.springbootchatmessenger.roles.EncryptionUtil.decrypt(userEntity.getAuthorities()))")
    UserDto toUserDto(final UserEntity userEntity);

    // partial updates

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity partialUpdate(final UserUpdateDto userUpdateDto, @MappingTarget final UserEntity userEntity);
}
