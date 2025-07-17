package com.example.messengerquery.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

@Setter
@Getter
@ToString
@Document(indexName = "sessions")
public class SessionDocument {

    @Id
    private String id;

    private UUID user1Id;

    private UUID user2Id;

    private Message messageEntity;
}
