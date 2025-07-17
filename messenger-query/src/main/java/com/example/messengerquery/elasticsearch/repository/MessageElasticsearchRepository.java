package com.example.messengerquery.elasticsearch.repository;

import com.example.messengerquery.model.MessageDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageElasticsearchRepository extends ElasticsearchRepository<MessageDocument, String> {

    Optional<MessageDocument> findBySessionId(UUID sessionId);

}
