package com.example.messengerquery.elasticsearch.index;

import com.example.messengerquery.aop.AfterThrowingException;
import com.example.messengerquery.elasticsearch.repository.MessageElasticsearchRepository;
import com.example.messengerquery.exception.IndexingException;
import com.example.messengerquery.mapper.MessageMapper;
import com.example.messengerquery.model.Message;
import com.example.messengerquery.model.MessageDocument;
import com.example.messengerquery.mysql.repository.MessageRepository;
import com.example.messengerutilities.utility.SyncEventType;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@AfterThrowingException
public class MessageIndexer implements Indexer<Message, MessageDocument> {

    private final MessageMapper messageMapper;

    private final MessageRepository databaseRepository;
    private final MessageElasticsearchRepository elasticsearchRepository;

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

            Page<Message> messages;

            do {
                messages = getAllMessages(page, size);

                final List<MessageDocument> messageDocumentList = messages.getContent()
                        .stream()
                        .map(this::map)
                        .toList();

                indexMessages(messageDocumentList);
            }
            while (!messages.isLast());

        }
        catch (Exception e) {
            throw new IndexingException(MessageDocument.class.getSimpleName());
        }
    }

    private Page<Message> getAllMessages(int page, int size) {
        return databaseRepository.findAll(PageRequest.of(page, size));
    }

    private void indexMessages(List<MessageDocument> messageDocumentList) {
        elasticsearchRepository.saveAll(messageDocumentList);

    }

    private MessageDocument map(Message message) {
        return messageMapper.toMessageDocument(message);
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

    private void insert(String id) {
        databaseRepository.findById(id)
                .ifPresent(inserted -> {
                    elasticsearchRepository.save(messageMapper.toMessageDocument(inserted));
                });
    }

    private void delete(String id) {
        elasticsearchRepository.findById(id).ifPresent(elasticsearchRepository::delete);
    }

    private void update(String id) {
        databaseRepository.findById(id)
                .ifPresent(updated -> {
                    elasticsearchRepository.save(messageMapper.toMessageDocument(updated));
                });

    }
}
