package com.example.messenger.user.model;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // dto to entity
    UserEntity toEntity(UserDto userDto);
    UserEntity toEntity(UserCreateDto userCreateDto);

    // entity to dto
    UserDto toUserDto(UserEntity userEntity);

    // partial updates
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity partialUpdate(UserUpdateDto userUpdateDto, @MappingTarget final UserEntity userEntity);

    // dto to dto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserUpdateDto userEntityToUpdateDto(UserEntity userEntity);
}
