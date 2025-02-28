package com.example.springbootchatmessenger.message;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageEntity toEntity(MessageDto messageDto);

    MessageDto toDto(MessageEntity messageEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MessageEntity partialUpdate(MessageDto messageDto, @MappingTarget MessageEntity messageEntity);
}
