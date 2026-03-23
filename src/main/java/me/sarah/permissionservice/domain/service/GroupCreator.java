package me.sarah.permissionservice.domain.service;

import me.sarah.permissionservice.domain.model.Group;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;

@Component
public class GroupCreator {

    public Group create(String name, String description) {
        return new Group(
                UUID.randomUUID(),
                name,
                description,
                new HashSet<>()
        );
    }
}
