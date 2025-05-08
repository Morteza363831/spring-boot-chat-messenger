package com.example.writekafka.mysql.handler;

import com.example.messengerutilities.utility.DataTypes;
import com.example.writekafka.utility.HandlerKey;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MySqlCommandHandlerFactory {

    private final Map<HandlerKey, MySqlCommandHandler<?>> handlerMap = new HashMap<>();

    public MySqlCommandHandlerFactory(MySqlCommandHandler<?>... handlers) {
        for (MySqlCommandHandler<?> handler : handlers) {
            HandlerKey key = determineHandlerKey(handler);
            handlerMap.put(key, handler);
        }
    }

    public  <T> MySqlCommandHandler<T> getHandler(DataTypes dataType) {
        HandlerKey key = new HandlerKey(dataType);
        return (MySqlCommandHandler<T>) handlerMap.get(key);
    }


    private HandlerKey determineHandlerKey(MySqlCommandHandler<?> handler) {

        if (handler instanceof MySqlUserCommandHandler) {
            return new HandlerKey(DataTypes.USER);
        }
        else if (handler instanceof MySqlSessionCommandHandler) {
            return new HandlerKey(DataTypes.SESSION);
        } else if (handler instanceof  MySqlMessageCommandHandler) {
            return new HandlerKey(DataTypes.MESSAGE);
        }
        // TODO
        throw new RuntimeException("Unknown MySqlCommandHandler");
    }
}
