package me.sarah.permissionservice.adapter.out.mongo.mapper;

import me.sarah.permissionservice.adapter.out.mongo.entity.GroupEntity;
import me.sarah.permissionservice.domain.model.Group;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class GroupPersistenceMapper {

    public Group toDomain(GroupEntity entity) {
        return new Group(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPermissions() == null
                ? new HashSet<>()
                        : new HashSet<>(entity.getPermissions())
        );
    }

    public GroupEntity toEntity(Group group) {
        GroupEntity entity = new GroupEntity();
        entity.setId(group.getId());
        entity.setName(group.getName());
        entity.setDescription(group.getDescription());
        entity.setPermissions(group.getPermissions());
        return entity;
    }
}
