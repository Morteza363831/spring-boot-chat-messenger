package com.example.springbootchatmessenger.session;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SessionMapper {

    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    SessionEntity sessionDtoToSessionEntity(final SessionEntityDto sessionEntityDto);

    SessionEntityDto sessionEntityToSessionDto(final SessionEntity sessionEntity);
}
