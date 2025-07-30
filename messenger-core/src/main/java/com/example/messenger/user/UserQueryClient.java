package com.example.messenger.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(
        name = "user-query",
        url = "http://localhost:8051/api/v1/query/user"
)
public interface UserQueryClient {

    @GetMapping("/{username}")
    Optional<UserEntity> getUser(@PathVariable String username);

    @GetMapping
    List<UserEntity> getUsers();

}
