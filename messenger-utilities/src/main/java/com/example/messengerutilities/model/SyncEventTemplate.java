package com.example.messengerutilities.model;

import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.SyncEventType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Map;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncEventTemplate {

    private SyncEventType type;
    private Map<DataTypes, String> ids;
}
