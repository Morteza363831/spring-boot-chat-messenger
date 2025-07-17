package com.example.messengerquery.Service;

import com.example.messengerquery.model.User;

public interface UserQueryService {

    User findByUsername(String username);

    boolean existsByUsername(String username);
}
