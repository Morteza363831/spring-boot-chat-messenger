package com.example.messengerquery.Service;

import com.example.messengerquery.elasticsearch.index.Indexer;
import com.example.messengerquery.elasticsearch.repository.UserElasticsearchRepository;
import com.example.messengerquery.mapper.UserMapper;
import com.example.messengerquery.model.User;
import com.example.messengerquery.model.UserDocument;
import com.example.messengerquery.mysql.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserMapper userMapper;

    private final UserElasticsearchRepository elasticsearchRepository;
    private final UserRepository userRepository;
    private final Indexer<User, UserDocument> userIndexer;

    @PostConstruct
    private void init() {
        userIndexer.reindex();
    }


    @Override
    public User findByUsername(String username) {
        // find user from index
        Optional<UserDocument> userDocumentOptional = elasticsearchRepository.findByUsername(username);
        // sync with db if not found
        if (userDocumentOptional.isEmpty()) {
            userDocumentOptional = syncUser(username);
        }
        return userDocumentOptional
                .map(userMapper::toUser)
                .orElse(null);
    }

    @Override
    public boolean existsByUsername(String username) {
        return elasticsearchRepository.existsByUsername(username);
    }

    @Override
    public List<User> findAll() {
        final List<User> retrievedUsers = new ArrayList<>();
        elasticsearchRepository.findAll().forEach(userDocument -> {
            retrievedUsers.add(userMapper.toUser(userDocument));
        });
        return retrievedUsers;
    }

    private Optional<UserDocument> syncUser(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toUserDocument)
                .map(elasticsearchRepository::save);
    }
}
