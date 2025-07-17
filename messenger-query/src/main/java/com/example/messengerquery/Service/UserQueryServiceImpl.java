package com.example.messengerquery.Service;

import com.example.messengerquery.elasticsearch.index.Indexing;
import com.example.messengerquery.elasticsearch.repository.UserElasticsearchRepository;
import com.example.messengerquery.mapper.UserMapper;
import com.example.messengerquery.model.User;
import com.example.messengerquery.model.UserDocument;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserMapper userMapper;

    private final UserElasticsearchRepository elasticsearchRepository;
    private final Indexing<User, UserDocument> userIndexing;


    @PostConstruct
    private void init() {
        userIndexing.index();
    }


    @Override
    public User findByUsername(String username) {

        return elasticsearchRepository.findByUsername(username)
                .map(userMapper::toUser)
                .orElseThrow(() -> new RuntimeException(""));
    }

    @Override
    public boolean existsByUsername(String username) {
        return elasticsearchRepository.existsByUsername(username);
    }
}
