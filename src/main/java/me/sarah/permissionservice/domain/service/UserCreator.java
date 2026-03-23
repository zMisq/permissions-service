package me.sarah.permissionservice.domain.service;

import me.sarah.permissionservice.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;

@Component
public class UserCreator {

    public User create(UUID id, String username, String email) {
        return new User(
                id,
                username,
                email,
                new HashSet<>(),
                new HashSet<>()
        );
    }
}


