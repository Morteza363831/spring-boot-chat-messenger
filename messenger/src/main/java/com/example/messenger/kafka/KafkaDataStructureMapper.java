package com.example.messenger.kafka;

import com.example.messenger.utility.DataTypes;
import com.example.messenger.utility.RequestTypes;
import org.mapstruct.*;

/**
 * This is an Adapter for KafkaDataStructure class
 * */

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface KafkaDataStructureMapper {

    @Mapping(target = "requestType", expression = "java(requestType)")
    @Mapping(target = "dataType", expression = "java(dataType)")
    @Mapping(target = "data", expression = "java(data)")
    KafkaDataStructure toKafkaDataStructure(Object data, @Context RequestTypes requestType, @Context DataTypes dataType);
}
