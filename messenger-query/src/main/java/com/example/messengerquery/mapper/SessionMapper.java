package com.example.messengerquery.mapper;

import com.example.messengerquery.model.Session;
import com.example.messengerquery.model.SessionDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface SessionMapper {

    @Mapping(target = "user1", expression = "java(UserMapper.INSTANCE.toUserDocument(session.getUser1()))")
    @Mapping(target = "user2", expression = "java(UserMapper.INSTANCE.toUserDocument(session.getUser2()))")
    SessionDocument toSessionDocument(Session session);

    Session toSession(SessionDocument sessionDocument);
}
