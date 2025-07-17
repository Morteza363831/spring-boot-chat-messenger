package com.example.messengercommand.mssql.handler;


import com.example.messengerutilities.utility.RequestTypes;

public interface MsSqlCommandHandler<T> {
    void handle(RequestTypes requestType, T t);
}
