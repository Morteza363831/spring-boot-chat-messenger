package com.example.messengerutilities.model;

import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@ToString
public class KafkaDataStructure {

    private String id = UUID.randomUUID().toString();
    private RequestTypes requestType;
    private DataTypes dataType;
    private Object data;

}
