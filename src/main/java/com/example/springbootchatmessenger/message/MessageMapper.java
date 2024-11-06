package com.example.springbootchatmessenger.message;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageEntity messageEntityDtoToMessageEntity(MessageEntityDto messageEntityDto);

    MessageEntityDto messageEntityToMessageEntityDto(MessageEntity messageEntity);

    default List<MessageEntity> messageEntityDtoListToMessageEntityList(List<MessageEntityDto> messageEntityDtoList){

        List<MessageEntity> messageEntityList = new ArrayList<>();
        for(MessageEntityDto messageEntityDto : messageEntityDtoList){
            MessageEntity messageEntity = messageEntityDtoToMessageEntity(messageEntityDto);
            messageEntityList.add(messageEntity);
        }
        return messageEntityList;
    }

    default List<MessageEntityDto> messageEntityListToMessageEntityDtoList(List<MessageEntity> messageEntityList){

        List<MessageEntityDto> messageEntityDtoList = new ArrayList<>();
        for(MessageEntity messageEntity : messageEntityList){
            MessageEntityDto messageEntityDto = messageEntityToMessageEntityDto(messageEntity);
            messageEntityDtoList.add(messageEntityDto);
        }
        return messageEntityDtoList;
    }
}
