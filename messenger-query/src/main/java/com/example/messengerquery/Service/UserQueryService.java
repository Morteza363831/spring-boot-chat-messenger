package com.example.messengerquery.Service;

import com.example.messengerquery.model.User;

import java.util.List;

public interface UserQueryService {

    User findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findAll();
}
