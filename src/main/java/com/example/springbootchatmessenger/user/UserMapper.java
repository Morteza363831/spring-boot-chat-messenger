package com.example.springbootchatmessenger.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity userDtoToUserEntity(final UserEntityDto userDto);

    UserEntityDto userEntityToUserDto(final UserEntity userEntity);

    default List<UserEntity> userDtoListToUserEntityList(final List<UserEntityDto> userEntityDtoList){

        //TODO

        final List<UserEntity> userEntityList = new ArrayList<>();

        userEntityDtoList.forEach(userEntityDto -> {
            userEntityList.add(userDtoToUserEntity(userEntityDto));
        });
        return userEntityList;
    }

    default List<UserEntityDto> userEntityListToUserDtoList(final List<UserEntity> userEntityList){

        //TODO

        final List<UserEntityDto> userEntityDtoList = new ArrayList<>();

        userEntityList.forEach(userEntity -> {
            userEntityDtoList.add(userEntityToUserDto(userEntity));
        });
        return userEntityDtoList;
    }
}
