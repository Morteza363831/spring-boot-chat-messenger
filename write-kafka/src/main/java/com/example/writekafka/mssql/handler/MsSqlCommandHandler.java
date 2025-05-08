package com.example.writekafka.mssql.handler;

import com.example.messenger.utility.RequestTypes;

public interface MsSqlCommandHandler<T> {
    void handle(RequestTypes requestType, T t);
}
