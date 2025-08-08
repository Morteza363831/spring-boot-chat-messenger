package com.example.messengerquery.elasticsearch.index;

import com.example.messengerquery.elasticsearch.repository.UserElasticsearchRepository;
import com.example.messengerquery.mapper.UserMapper;
import com.example.messengerquery.model.User;
import com.example.messengerquery.model.UserDocument;
import com.example.messengerquery.mysql.repository.UserRepository;
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
public class UserIndexer implements Indexer<User, UserDocument> {

    private final UserMapper userMapper;

    private final UserRepository databaseRepository;
    private final UserElasticsearchRepository elasticsearchRepository;

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

        int page = 0;
        int size = 500;

        Page<User> users;

        do {
            users = getAllUsers(page, size);

            List<UserDocument> userDocumentList = users.getContent()
                    .stream()
                    .map(this::map)
                    .toList();

            indexUsers(userDocumentList);
        }
        while (!users.isLast());
    }

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
                    elasticsearchRepository.save(userMapper.toUserDocument(inserted));
                });
    }

    private void delete(String id) {
        elasticsearchRepository.findById(id)
                .ifPresent(elasticsearchRepository::delete);
    }

    private void update(String id) {
        databaseRepository.findById(id)
                .ifPresent(user -> {
                    elasticsearchRepository.save(userMapper.toUserDocument(user));
                });
    }

    private Page<User> getAllUsers(int page, int size) {
        return databaseRepository.findAll(PageRequest.of(page, size));
    }

    private void indexUsers(List<UserDocument> users) {
        try {
            elasticsearchRepository.saveAll(users);
        }
        catch (Exception e) {
            log.error(e.getStackTrace().toString());
        }
    }

    private UserDocument map(User user) {
        return userMapper.toUserDocument(user);
    }
}
