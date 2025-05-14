package com.develop.springguard.domain.auth.repository;

import com.develop.springguard.domain.auth.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    private final Map<String, User> usersByUsername = new HashMap<>();
    private final Map<Long, User> usersById = new HashMap<>();
    private long nextId = 1;

    @PostConstruct
    public void init() {
        // 서버가 시작될 때마다 초기화
        usersByUsername.clear();
        usersById.clear();
        nextId = 1;
        System.out.println("UserRepository initialized.");
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public void save(User user) {
        user.setId(nextId++);
        usersByUsername.put(user.getUsername(), user);
        usersById.put(user.getId(), user);
    }
}