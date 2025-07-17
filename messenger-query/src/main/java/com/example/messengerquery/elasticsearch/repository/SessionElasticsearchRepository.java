package com.example.messengerquery.elasticsearch.repository;

import com.example.messengerquery.model.SessionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionElasticsearchRepository extends ElasticsearchRepository<SessionDocument, String> {


    @Query("""
{
    "bool": {
        "should": [
            {
                "bool" : {
                    "must" : [
                        {"term" : {"user1Id" : "?0"}},
                        {"term" : {"user2Id" : "?1"}}
                    ]
                }
            },
            {
                "bool" : {
                    "must" : [
                        {"term" : {"user1Id" : "?1"}},
                        {"term" : {"user2Id" : "?0"}}
                    ]
                }
            }
        ]
    }
}""")
    Optional<SessionDocument> findByUser1IdAndUser2Id(UUID user1Id, UUID user2Id);
}
