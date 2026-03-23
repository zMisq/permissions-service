package me.sarah.permissionservice.port.out;

import me.sarah.permissionservice.domain.model.Group;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepositoryPort {

    Optional<Group> findById(UUID groupId);

    Optional<Group> findByName(String name);

    Group save(Group group);
}
