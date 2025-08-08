package com.example.messengerquery.elasticsearch.consumer;

import com.example.messengerquery.elasticsearch.index.Indexer;
import com.example.messengerquery.model.*;
import com.example.messengerutilities.model.SyncEventTemplate;
import com.example.messengerutilities.utility.DataTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class SyncEventDispatcher {

    private final Indexer<User, UserDocument> userIndexer;
    private final Indexer<Session, SessionDocument> sessionIndexer;
    private final Indexer<Message, MessageDocument> messageIndexer;

    public void dispatch(SyncEventTemplate syncEventTemplate) {
        Set<DataTypes> keys = syncEventTemplate.getIds().keySet();

        keys.forEach(key -> {
            switch (key) {
                case USER -> userIndexer.sync(syncEventTemplate.getType(), syncEventTemplate.getIds().get(key));
                case SESSION -> sessionIndexer.sync(syncEventTemplate.getType(), syncEventTemplate.getIds().get(key));
                case MESSAGE -> messageIndexer.sync(syncEventTemplate.getType(), syncEventTemplate.getIds().get(key));
            }
        });
    }
}
