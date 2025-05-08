package com.example.writekafka.mssql.handler;

import com.example.messengerutilities.model.KafkaDataStructure;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.writekafka.model.Message;
import com.example.writekafka.model.Session;
import com.example.writekafka.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *  Dispatchers are same for now ! (because we are using two relational database)
 * */

@Service
@RequiredArgsConstructor
public class MsSqlCommandDispatcher {

    private final MsSqlCommandHandlerFactory msSqlCommandHandlerFactory;

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

            default -> throw new RuntimeException("Unexpected value: " + dataType);
        }
    }


    private User convertToUser(Object data) {
        if (data instanceof User) {
            return (User) data;
        }
        return null;
    }

    private Session convertToSession(Object data) {
        if (data instanceof Session) {
            return (Session) data;
        }
        return null;
    }

    private Message convertToMessage(Object data) {
        if (data instanceof Message) {
            return (Message) data;
        }
        return null;
    }
}
