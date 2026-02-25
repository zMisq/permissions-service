package me.sarah.permissionservice.application.service;

import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.domain.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {

    User createUser(String username);

    List<User> getAllUsers();

    User getUser(UUID id);

    void addPermissionToUser (UUID userId, String permission);

    Set<String> getEffectivePermissions(UUID userId);

    void addUserToGroup(UUID id, Group group);
}
