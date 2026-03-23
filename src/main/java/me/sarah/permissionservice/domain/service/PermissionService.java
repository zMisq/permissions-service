package me.sarah.permissionservice.domain.service;

import me.sarah.permissionservice.domain.exception.UserNotFoundException;
import me.sarah.permissionservice.domain.model.User;
import me.sarah.permissionservice.port.in.PermissionUseCase;
import me.sarah.permissionservice.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class PermissionService implements PermissionUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PermissionResolver permissionResolver;

    public PermissionService(UserRepositoryPort userRepositoryPort, PermissionResolver permissionResolver) {
        this.userRepositoryPort = userRepositoryPort;
        this.permissionResolver = permissionResolver;
    }

    @Override
    public User assignDirectPermission(UUID userId, String permission) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.addPermission(permission);
        return userRepositoryPort.save(user);
    }

    @Override
    public User removeDirectPermission(UUID userId, String permission) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.removePermission(permission);
        return userRepositoryPort.save(user);
    }

    @Override
    public Set<String> getEffectivePermissions(UUID userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return permissionResolver.resolvePermissions(user);
    }

    @Override
    public boolean hasPermission(UUID userId, String permission) {
        Set<String> effectivePermissions = getEffectivePermissions(userId);
        return effectivePermissions.contains(permission);
    }
}
