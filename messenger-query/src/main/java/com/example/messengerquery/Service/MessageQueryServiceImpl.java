package com.example.messengerquery.Service;

import com.example.messengerquery.elasticsearch.index.Indexing;
import com.example.messengerquery.elasticsearch.repository.MessageElasticsearchRepository;
import com.example.messengerquery.mapper.MessageMapper;
import com.example.messengerquery.model.Message;
import com.example.messengerquery.model.MessageDocument;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageQueryServiceImpl implements MessageQueryService {

    private final MessageMapper messageMapper;

    private final MessageElasticsearchRepository elasticsearchRepository;
    private final Indexing<Message, MessageDocument> messageIndexing;


    @PostConstruct
    private void init() {
        messageIndexing.index();
    }


    @Override
    public Message findBySessionId(UUID sessionId) {
        return elasticsearchRepository.findBySessionId(sessionId)
                .map(messageMapper::toMessage)
                .orElseThrow(() -> new RuntimeException(""));
    }
}
