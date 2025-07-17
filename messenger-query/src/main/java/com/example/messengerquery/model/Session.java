package com.example.messengerquery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "sessions", uniqueConstraints = {
        @UniqueConstraint(name = "uq_session_users", columnNames = {"user1_id", "user2_id"}),
        @UniqueConstraint(name = "message_entity_id", columnNames = {"message_entity"})
})
public class Session {
    @Id
    @Size(max = 16)
    @Column(name = "id", nullable = false, length = 16)
    private String id;

    @Size(max = 16)
    @NotNull
    @Column(name = "user1_id", nullable = false, length = 16)
    private UUID user1Id;

    @Size(max = 16)
    @NotNull
    @Column(name = "user2_id", nullable = false, length = 16)
    private UUID user2Id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_entity")
    private Message messageEntity;

}