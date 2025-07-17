package com.example.messengerquery.mapper;

import com.example.messengerquery.model.Message;
import com.example.messengerquery.model.MessageDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {

    MessageDocument toMessageDocument(Message message);

    Message toMessage(MessageDocument messageDocument);

}
