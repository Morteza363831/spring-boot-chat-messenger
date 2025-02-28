package com.example.springbootchatmessenger.session;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SessionMapper {

    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    // dto to entity

    @Mapping(source = "user1", target = "user1.username")
    @Mapping(source = "user2", target = "user2.username")
    SessionEntity toEntity(SessionCreateDto sessionCreateDto);

    // entity to dto

    @Mapping(source = "user1.username", target = "user1")
    @Mapping(source = "user2.username", target = "user2")
    SessionDto sessionEntityToSessionDto(final SessionEntity sessionEntity);

    // dto to dto

    SessionCreateDto findDtoToCreateDto(SessionFindDto sessionFindDto);

    // update maps

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "user1", target = "user1.username")
    @Mapping(source = "user2", target = "user2.username")
    SessionEntity partialUpdate(SessionCreateDto sessionCreateDto, @MappingTarget SessionEntity sessionEntity);
}
