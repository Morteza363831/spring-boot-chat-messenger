package com.example.messengerquery.mapper;

import com.example.messengerquery.model.Session;
import com.example.messengerquery.model.SessionDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface SessionMapper {

    SessionDocument toSessionDocument(Session session);

    Session toSession(SessionDocument sessionDocument);
}
