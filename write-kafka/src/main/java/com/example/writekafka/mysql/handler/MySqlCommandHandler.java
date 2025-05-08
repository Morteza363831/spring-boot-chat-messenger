package com.example.writekafka.mysql.handler;


import com.example.messengerutilities.utility.RequestTypes;

public interface MySqlCommandHandler<T> {

    void handle(RequestTypes requestType, T t);
}
