package com.example.messengerquery.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

@Setter
@Getter
@ToString
@Document(indexName = "messages")
public class MessageDocument {

    @Id
    private String id;

    private String content;

    private UUID sessionId;

    private String encryptedAesKey;
}
