package com.example.messenger.kafka;

import com.example.messenger.utility.DataTypes;
import com.example.messenger.utility.RequestTypes;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class KafkaDataStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private RequestTypes requestType;
    private DataTypes dataType;
    private Object data;

}
