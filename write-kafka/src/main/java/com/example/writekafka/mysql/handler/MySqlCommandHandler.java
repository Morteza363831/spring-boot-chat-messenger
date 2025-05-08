package com.example.writekafka.mysql.handler;

import com.example.messenger.utility.RequestTypes;

public interface MySqlCommandHandler<T> {

    void handle(RequestTypes requestType, T t);
}
