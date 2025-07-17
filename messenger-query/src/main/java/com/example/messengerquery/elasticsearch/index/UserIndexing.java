package com.example.messengerquery.elasticsearch.index;

import com.example.messengerquery.elasticsearch.repository.UserElasticsearchRepository;
import com.example.messengerquery.mapper.UserMapper;
import com.example.messengerquery.model.User;
import com.example.messengerquery.model.UserDocument;
import com.example.messengerquery.mysql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserIndexing implements Indexing<User, UserDocument> {

    private final UserMapper userMapper;

    private final UserRepository databaseRepository;
    private final UserElasticsearchRepository elasticsearchRepository;

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
