package com.example.messengerquery.Service;

import com.example.messengerquery.model.Message;
import com.example.messengerquery.model.Session;

import java.util.UUID;

public interface MessageQueryService {

    Message findBySessionId(UUID sessionId);
}
