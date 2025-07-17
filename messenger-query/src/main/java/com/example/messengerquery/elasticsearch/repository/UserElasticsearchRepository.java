package com.example.messengerquery.elasticsearch.repository;

import com.example.messengerquery.model.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserElasticsearchRepository extends ElasticsearchRepository<UserDocument, String> {

    Optional<UserDocument> findByUsername(String username);

    boolean existsByUsername(String username);
}
