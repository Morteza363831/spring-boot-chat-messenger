package com.example.messengercommand.mysql.handler;

import com.example.messengerutilities.model.KafkaDataStructure;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengercommand.model.Message;
import com.example.messengercommand.model.Session;
import com.example.messengercommand.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *  Dispatchers are same for now ! (because we are using two relational database)
 * */

@Slf4j
@Service
@RequiredArgsConstructor
public class MySqlCommandDispatcher {

    private final ObjectMapper mapper;

    private final MySqlCommandHandlerFactory mySqlCommandHandlerFactory;

    public void dispatch(KafkaDataStructure kafkaDataStructure) {
        DataTypes dataType = kafkaDataStructure.getDataType();
        RequestTypes requestType = kafkaDataStructure.getRequestType();

        switch (dataType) {
            case USER -> mySqlCommandHandlerFactory.<User>getHandler(DataTypes.USER)
                    .handle(requestType, convertToUser(kafkaDataStructure.getData()));

            case SESSION -> mySqlCommandHandlerFactory.<Session>getHandler(DataTypes.SESSION)
                    .handle(requestType, convertToSession(kafkaDataStructure.getData()));

            case MESSAGE -> mySqlCommandHandlerFactory.<Message>getHandler(DataTypes.MESSAGE)
                    .handle(requestType, convertToMessage(kafkaDataStructure.getData()));

            default -> throw new RuntimeException("Unexpected value: " + dataType);
        }
    }


    private User convertToUser(Object data) {
        try {
            return mapper.convertValue(data, User.class);
        }
        catch (Exception e) {
            log.error("Error converting data to user", e);
            return null;
        }
    }

    private Session convertToSession(Object data) {
        try {
            return mapper.convertValue(data, Session.class);
        }
        catch (Exception e) {
            log.error("Error converting data to session", e);
            return null;
        }
    }

    private Message convertToMessage(Object data) {
        try {
            return mapper.convertValue(data, Message.class);
        }
        catch (Exception e) {
            log.error("Error converting data to message", e);
            return null;
        }
    }
}
