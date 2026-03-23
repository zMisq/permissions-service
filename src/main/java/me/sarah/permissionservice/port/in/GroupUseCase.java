package me.sarah.permissionservice.port.in;

import me.sarah.permissionservice.domain.model.Group;

import java.util.UUID;

public interface GroupUseCase {
    Group createGroup(String name, String description);

    Group getGroupById(UUID groupId);

    Group assignPermission(UUID groupId, String permission);

    Group removePermission(UUID groupId, String permission);
}
