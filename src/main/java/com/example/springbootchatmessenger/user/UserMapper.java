package com.example.springbootchatmessenger.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity userDtoToUserEntity(UserEntityDto userDto);

    UserEntityDto userEntityToUserDto(UserEntity userEntity);

    default List<UserEntity> userDtoListToUserEntityList(List<UserEntityDto> userEntityDtoList){

        List<UserEntity> userEntityList = new ArrayList<>();

        for (UserEntityDto userEntityDto : userEntityDtoList) {
            UserEntity userEntity = userDtoToUserEntity(userEntityDto);
            userEntityList.add(userEntity);
        }
        return userEntityList;
    }

    default List<UserEntityDto> userEntityListToUserDtoList(List<UserEntity> userEntityList){

        List<UserEntityDto> userEntityDtoList = new ArrayList<>();

        for (UserEntity userEntity : userEntityList) {
            userEntityDtoList.add(userEntityToUserDto(userEntity));
        }
        return userEntityDtoList;
    }
}
