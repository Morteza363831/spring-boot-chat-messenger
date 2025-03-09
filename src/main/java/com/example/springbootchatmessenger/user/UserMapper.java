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
    UserEntity toEntity(final UserCreateDto userCreateDto);
    default List<UserEntity> userDtoListToUserEntityList(final List<UserDto> userDtoList) {

        if (userDtoList == null) {
            return new ArrayList<>();
        }

        final List<UserEntity> userEntityList = new ArrayList<>();

        userDtoList.forEach(userEntityDto -> {
            userEntityList.add(toEntity(userEntityDto));
        });
        return userEntityList;
    }


    // entity to dto
    default List<UserDto> userEntityListToUserDtoList(final List<UserEntity> userEntityList) {

        if (userEntityList == null) {
            return new ArrayList<>();
        }

        final List<UserDto> userDtoList = new ArrayList<>();

        userEntityList.forEach(userEntity -> {
            userDtoList.add(toUserDto(userEntity));
        });
        return userDtoList;
    }
    @Mapping(target = "authorities", expression = "java(com.example.springbootchatmessenger.utility.EncryptionUtil.decrypt(userEntity.getAuthorities()))")
    UserDto toUserDto(final UserEntity userEntity);

    // partial updates
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity partialUpdate(final UserUpdateDto userUpdateDto, @MappingTarget final UserEntity userEntity);

    // dto to dto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserUpdateDto userDtoToUpdateDto(final UserDto userDto);
}
