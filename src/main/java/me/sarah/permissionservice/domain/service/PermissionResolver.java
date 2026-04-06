package me.sarah.permissionservice.domain.service;

import me.sarah.permissionservice.domain.exception.GroupNotFoundException;
import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.domain.model.User;
import me.sarah.permissionservice.port.out.GroupRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Component
public class PermissionResolver {

    private final GroupRepositoryPort groupRepositoryPort;

    public PermissionResolver(GroupRepositoryPort groupRepositoryPort) {
        this.groupRepositoryPort = groupRepositoryPort;
    }

    public Set<String> resolvePermissions(User user) {
        Set<String> effectivePermissions = new HashSet<>(user.getDirectPermissions());

        for (UUID groupId : user.getGroupIds()) {
            Group group = groupRepositoryPort.findById(groupId)
                    .orElseThrow(() -> new GroupNotFoundException(groupId));

            effectivePermissions.addAll(group.getPermissions());
        }

        return effectivePermissions;
    }
}
