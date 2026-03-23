package me.sarah.permissionservice.domain.service;

import me.sarah.permissionservice.domain.exception.GroupAlreadyExistsException;
import me.sarah.permissionservice.domain.exception.GroupNotFoundException;
import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.port.in.GroupUseCase;
import me.sarah.permissionservice.port.out.GroupRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GroupService implements GroupUseCase {

    private final GroupRepositoryPort groupRepositoryPort;
    private final GroupCreator groupCreator;

    public GroupService(GroupRepositoryPort groupRepositoryPort, GroupCreator groupCreator) {
        this.groupRepositoryPort = groupRepositoryPort;
        this.groupCreator = groupCreator;
    }

    @Override
    public Group createGroup(String name, String description) {
        groupRepositoryPort.findByName(name)
                .ifPresent(group -> {
                    throw new GroupAlreadyExistsException(group.getName());
                });
        Group group = groupCreator.create(name, description);
        return groupRepositoryPort.save(group);
    }

    @Override
    public Group getGroupById(UUID groupId) {
        return groupRepositoryPort.findById((groupId))
                .orElseThrow(() -> new GroupNotFoundException(groupId));
    }

    @Override
    public Group assignPermission(UUID groupId, String permission) {
        Group group = groupRepositoryPort.findById((groupId))
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        group.addPermission(permission);
        return groupRepositoryPort.save(group);
    }

    @Override
    public Group removePermission(UUID groupId, String permission) {
        Group group = groupRepositoryPort.findById((groupId))
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        group.removePermission(permission);
        return groupRepositoryPort.save(group);
    }
}
