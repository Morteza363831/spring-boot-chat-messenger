package com.example.messengercommand.mssql.handler;

import com.example.messengercommand.aop.AfterThrowingException;
import com.example.messengercommand.exceptions.ConvertObjectException;
import com.example.messengercommand.exceptions.UnExpectedValueException;
import com.example.messengerutilities.model.KafkaDataStructure;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengercommand.model.Message;
import com.example.messengercommand.model.Session;
import com.example.messengercommand.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *  Dispatchers are same for now ! (because we are using two relational database)
 * */

@Slf4j
@Service
@RequiredArgsConstructor
@AfterThrowingException
public class MsSqlCommandDispatcher {

    private final ObjectMapper mapper;

    private final MsSqlCommandHandlerFactory msSqlCommandHandlerFactory;

    @Transactional(rollbackOn = Exception.class)
    public void dispatch(KafkaDataStructure kafkaDataStructure) {
        DataTypes dataType = kafkaDataStructure.getDataType();
        RequestTypes requestType = kafkaDataStructure.getRequestType();

        switch (dataType) {
            case USER -> msSqlCommandHandlerFactory.<User>getHandler(DataTypes.USER)
                    .handle(requestType, convertToUser(kafkaDataStructure.getData()));

            case SESSION -> msSqlCommandHandlerFactory.<Session>getHandler(DataTypes.SESSION)
                    .handle(requestType, convertToSession(kafkaDataStructure.getData()));

            case MESSAGE -> msSqlCommandHandlerFactory.<Message>getHandler(DataTypes.MESSAGE)
                    .handle(requestType, convertToMessage(kafkaDataStructure.getData()));

            default -> throw new UnExpectedValueException(dataType);
        }
    }


    private User convertToUser(Object data) {
        try {
            return mapper.convertValue(data, User.class);
        }
        catch (Exception e) {
            throw new ConvertObjectException(User.class.getSimpleName());
        }
    }

    private Session convertToSession(Object data) {
        try {
            return mapper.convertValue(data, Session.class);
        }
        catch (Exception e) {
            throw new ConvertObjectException(Session.class.getSimpleName());
        }
    }

    private Message convertToMessage(Object data) {
        try {
            return mapper.convertValue(data, Message.class);
        }
        catch (Exception e) {
            throw new ConvertObjectException(Message.class.getSimpleName());
        }
    }
}
