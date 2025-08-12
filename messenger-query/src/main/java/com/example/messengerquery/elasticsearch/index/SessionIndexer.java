package com.example.messengerquery.elasticsearch.index;

import com.example.messengerquery.aop.AfterThrowingException;
import com.example.messengerquery.elasticsearch.repository.SessionElasticsearchRepository;
import com.example.messengerquery.exception.IndexingException;
import com.example.messengerquery.mapper.SessionMapper;
import com.example.messengerquery.model.Session;
import com.example.messengerquery.model.SessionDocument;
import com.example.messengerquery.mysql.repository.SessionRepository;
import com.example.messengerutilities.utility.SyncEventType;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@AfterThrowingException
public class SessionIndexer implements Indexer<Session, SessionDocument> {

    private final SessionMapper sessionMapper;

    private final SessionRepository databaseRepository;
    private final SessionElasticsearchRepository elasticsearchRepository;

    @PreDestroy
    private void unIndex() {
        elasticsearchRepository.deleteAll();
    }

    @Override
    public void reindex() {
        unIndex();
        index();
    }

    @Override
    public void index() {

        try {

            int page = 0;
            int size = 500;
            Page<Session> sessions;

            do {
                sessions = getAllSessions(page, size);

                final List<SessionDocument> sessionDocumentList = sessions.getContent()
                        .stream()
                        .map(this::map)
                        .toList();

                indexSessions(sessionDocumentList);
            }
            while (!sessions.isLast());

        }
        catch (Exception e) {
            throw new IndexingException(SessionDocument.class.getSimpleName());
        }

    }

    private Page<Session> getAllSessions(int page, int size) {
        return databaseRepository.findAllEntityGraph(PageRequest.of(page, size));
    }

    private void indexSessions(List<SessionDocument> sessionDocumentList) {
        elasticsearchRepository.saveAll(sessionDocumentList);
    }

    private SessionDocument map(Session session) {
        return sessionMapper.toSessionDocument(session);
    }


    // It is not following solid principles .
    @Override
    public void sync(SyncEventType type, String id) {
        switch (type) {
            case INSERT -> insert(id);
            case UPDATE -> update(id);
            case DELETE -> delete(id);
        }
    }

    private void insert(String id){
        databaseRepository.findById(id)
                .ifPresent(inserted -> {
            elasticsearchRepository.save(sessionMapper.toSessionDocument(inserted));
        });
    }

    private void delete(String id){
        elasticsearchRepository.findById(id)
                .ifPresent(elasticsearchRepository::delete);
    }

    private void update(String id){
        databaseRepository.findById(id)
                .ifPresent(updated -> {
            elasticsearchRepository.save(sessionMapper.toSessionDocument(updated));
        });
    }
}
