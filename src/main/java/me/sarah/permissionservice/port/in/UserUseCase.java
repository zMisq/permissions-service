package me.sarah.permissionservice.port.in;

import me.sarah.permissionservice.domain.model.User;

import java.util.UUID;

public interface UserUseCase {
    User createUser(String username, String email);

    User getUserById(UUID userId);

    User addUserToGroup(UUID userId, UUID groupId);

    User removeUserFromGroup(UUID userId, UUID groupId);

}

