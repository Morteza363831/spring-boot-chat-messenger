package com.example.messengerquery.elasticsearch.index;

import com.example.messengerutilities.utility.SyncEventType;

public interface Indexer<S,T> {

    void index();
    void reindex();
    void sync(SyncEventType type, String id);
}
