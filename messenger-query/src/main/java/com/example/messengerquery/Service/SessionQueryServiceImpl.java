package com.example.messengerquery.Service;

import com.example.messengerquery.elasticsearch.index.Indexing;
import com.example.messengerquery.elasticsearch.repository.SessionElasticsearchRepository;
import com.example.messengerquery.mapper.SessionMapper;
import com.example.messengerquery.model.Session;
import com.example.messengerquery.model.SessionDocument;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionQueryServiceImpl implements SessionQueryService {

    private final SessionMapper sessionMapper;

    private final SessionElasticsearchRepository elasticSearchRepository;
    private final Indexing<Session, SessionDocument> sessionIndexing;


    @PostConstruct
    private void init() {
        sessionIndexing.index();
    }


    @Override
    public Session findByUser1AndUser2(UUID user1Id, UUID user2Id) {
        return elasticSearchRepository.findByUser1IdAndUser2Id(user1Id, user2Id)
                .map(sessionMapper::toSession)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }
}
