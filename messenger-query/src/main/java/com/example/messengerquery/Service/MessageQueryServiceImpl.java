package com.example.messengerquery.Service;

import com.example.messengerquery.elasticsearch.index.Indexing;
import com.example.messengerquery.elasticsearch.repository.MessageElasticsearchRepository;
import com.example.messengerquery.mapper.MessageMapper;
import com.example.messengerquery.model.Message;
import com.example.messengerquery.model.MessageDocument;
import com.example.messengerquery.mysql.repository.MessageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageQueryServiceImpl implements MessageQueryService {

    private final MessageMapper messageMapper;

    private final MessageElasticsearchRepository elasticsearchRepository;
    private final MessageRepository messageRepository;
    private final Indexing<Message, MessageDocument> messageIndexing;

    @PostConstruct
    private void init() {
        messageIndexing.reindex();
    }


    @Override
    public Message findBySessionId(UUID sessionId) {
        // find message from index
        Optional<MessageDocument> messageDocumentOptional = elasticsearchRepository.findBySessionId(sessionId.toString());
        // sync if not found
        if (messageDocumentOptional.isEmpty()) {
            messageDocumentOptional = syncMessage(sessionId);
        }
        return messageDocumentOptional
                .map(messageMapper::toMessage)
                .orElse(null);
    }


    private Optional<MessageDocument> syncMessage(UUID sessionId) {
        return messageRepository.findBySessionId(sessionId.toString())
                .map(messageMapper::toMessageDocument)
                .map(elasticsearchRepository::save);
    }
}
