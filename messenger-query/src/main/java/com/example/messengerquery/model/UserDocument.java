package com.example.messengerquery.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

@Setter
@Getter
@ToString
@Document(indexName = "users")
public class UserDocument {

    @Id
    private String id;

    private String username;

    private Boolean enabled = false;

    private String email;

    private String firstName;

    private String lastName;

    private String authorities;

    private String password;
}
