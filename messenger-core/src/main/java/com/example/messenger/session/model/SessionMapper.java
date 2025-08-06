package com.example.messenger.session.model;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface SessionMapper {

    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    // entity to dto
    @Mapping(source = "user1.username", target = "user1")
    @Mapping(source = "user2.username", target = "user2")
    SessionDto sessionEntityToSessionDto(final SessionEntity sessionEntity);
}
