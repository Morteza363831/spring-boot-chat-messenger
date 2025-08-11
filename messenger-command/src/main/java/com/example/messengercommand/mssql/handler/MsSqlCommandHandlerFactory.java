package com.example.messengercommand.mssql.handler;

import com.example.messengercommand.aop.AfterThrowingException;
import com.example.messengercommand.exceptions.UnknownException;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengercommand.utility.HandlerKey;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@AfterThrowingException
public class MsSqlCommandHandlerFactory {

    private final HashMap<HandlerKey, MsSqlCommandHandler<?>> handlerMap = new HashMap<>();

    public MsSqlCommandHandlerFactory(MsSqlCommandHandler<?>... handlers) {
        for (MsSqlCommandHandler<?> handler : handlers) {
            HandlerKey key = determineHandlerKey(handler);
            handlerMap.put(key, handler);
        }
    }

    public <T> MsSqlCommandHandler<T> getHandler(DataTypes dataType) {
        HandlerKey key = new HandlerKey(dataType);
        return (MsSqlCommandHandler<T>) handlerMap.get(key);
    }


    private HandlerKey determineHandlerKey(MsSqlCommandHandler<?> handler) {
        if (handler instanceof MsSqlUserCommandHandler) {
            return new HandlerKey(DataTypes.USER);
        } else if (handler instanceof MsSqlSessionCommandHandler) {
            return new HandlerKey(DataTypes.SESSION);
        } else if (handler instanceof MsSqlMessageCommandHandler) {
            return new HandlerKey(DataTypes.MESSAGE);
        }
        // TODO
        throw new UnknownException("Unknown MsSqlCommandHandler");
    }
}
