package com.example.messengerquery.elasticsearch.index;

import com.example.messengerquery.elasticsearch.repository.MessageElasticsearchRepository;
import com.example.messengerquery.mapper.MessageMapper;
import com.example.messengerquery.model.Message;
import com.example.messengerquery.model.MessageDocument;
import com.example.messengerquery.mysql.repository.MessageRepository;
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
public class MessageIndexing implements Indexing<Message, MessageDocument> {

    private final MessageMapper messageMapper;

    private final MessageRepository databaseRepository;
    private final MessageElasticsearchRepository elasticsearchRepository;

    @Override
    public void index() {

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

    private Page<Message> getAllMessages(int page, int size) {
        return databaseRepository.findAll(PageRequest.of(page, size));
    }

    private void indexMessages(List<MessageDocument> messageDocumentList) {
        try {
            elasticsearchRepository.saveAll(messageDocumentList);
        }
        catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    private MessageDocument map(Message message) {
        return messageMapper.toMessageDocument(message);
    }
}
