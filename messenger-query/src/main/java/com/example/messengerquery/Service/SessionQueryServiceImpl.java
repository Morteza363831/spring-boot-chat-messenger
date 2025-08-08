package com.example.messengerquery.Service;

import com.example.messengerquery.elasticsearch.index.Indexer;
import com.example.messengerquery.elasticsearch.repository.SessionElasticsearchRepository;
import com.example.messengerquery.mapper.SessionMapper;
import com.example.messengerquery.model.Session;
import com.example.messengerquery.model.SessionDocument;
import com.example.messengerquery.mysql.repository.SessionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionQueryServiceImpl implements SessionQueryService {

    private final SessionMapper sessionMapper;

    private final SessionElasticsearchRepository elasticSearchRepository;
    private final SessionRepository sessionRepository;
    private final Indexer<Session, SessionDocument> sessionIndexer;

    @PostConstruct
    private void init() {
        sessionIndexer.reindex();
    }


    @Override
    public Session findByUser1AndUser2(UUID user1Id, UUID user2Id) {
        // query for getting session
        Optional<SessionDocument> sessionOptional = elasticSearchRepository.findByUser1IdAndUser2Id(user1Id.toString(), user2Id.toString());
        if (sessionOptional.isEmpty()) {
            sessionOptional = elasticSearchRepository.findByUser1IdAndUser2Id(user2Id.toString(), user1Id.toString());
        }
        // if session not found in index
        if (sessionOptional.isEmpty()) {
            sessionOptional = syncSession(user1Id, user2Id);
        }
        return sessionOptional
                .map(sessionMapper::toSession)
                .orElse(null);
    }

    private Optional<SessionDocument> syncSession(UUID user1Id, UUID user2Id) {
        return sessionRepository.findExistingSession(user1Id.toString(), user2Id.toString())
                .map(sessionMapper::toSessionDocument)
                .map(elasticSearchRepository::save);
    }


}
