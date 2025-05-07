package com.example.messenger.message;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageEntity toEntity(MessageDto messageDto);

    MessageDto toDto(MessageEntity messageEntity);
}
