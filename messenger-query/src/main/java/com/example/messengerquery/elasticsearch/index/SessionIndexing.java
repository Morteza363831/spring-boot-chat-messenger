package com.example.messengerquery.elasticsearch.index;

import com.example.messengerquery.elasticsearch.repository.SessionElasticsearchRepository;
import com.example.messengerquery.mapper.SessionMapper;
import com.example.messengerquery.model.Session;
import com.example.messengerquery.model.SessionDocument;
import com.example.messengerquery.mysql.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionIndexing implements Indexing<Session, SessionDocument> {

    private final SessionMapper sessionMapper;

    private final SessionRepository databaseRepository;
    private final SessionElasticsearchRepository elasticsearchRepository;


    @Override
    public void index() {

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

    private Page<Session> getAllSessions(int page, int size) {
        return databaseRepository.findAll(PageRequest.of(page, size));
    }

    private void indexSessions(List<SessionDocument> sessionDocumentList) {
        try {
            elasticsearchRepository.saveAll(sessionDocumentList);
        }
        catch (Exception e) {
            log.error(e.getStackTrace().toString());
        }
    }

    private SessionDocument map(Session session) {
        return sessionMapper.toSessionDocument(session);
    }


}
