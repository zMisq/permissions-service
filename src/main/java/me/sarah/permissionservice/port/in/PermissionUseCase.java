package me.sarah.permissionservice.port.in;

import me.sarah.permissionservice.domain.model.User;

import java.util.Set;
import java.util.UUID;

public interface PermissionUseCase {
    User assignDirectPermission(UUID userId, String permission);

    User removeDirectPermission(UUID userId, String permission);

    Set<String> getEffectivePermissions(UUID userId);

    boolean hasPermission(UUID userId, String permission);
}
