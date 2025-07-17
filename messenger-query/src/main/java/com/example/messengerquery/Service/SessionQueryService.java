package com.example.messengerquery.Service;


import com.example.messengerquery.model.Session;

import java.util.UUID;

public interface SessionQueryService {

    Session findByUser1AndUser2(UUID user1Id, UUID user2Id);
}
