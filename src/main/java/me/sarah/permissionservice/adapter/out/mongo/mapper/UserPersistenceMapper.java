package me.sarah.permissionservice.adapter.out.mongo.mapper;

import me.sarah.permissionservice.adapter.out.mongo.entity.UserEntity;
import me.sarah.permissionservice.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserPersistenceMapper {

    public User toDomain(UserEntity entity) {
        Set<String> directPermissions = entity.getDirectPermissions() == null
                ? new HashSet<>()
                : new HashSet<>(entity.getDirectPermissions());

        Set<UUID> groupIds = entity.getGroupIds() == null
                ? new HashSet<>()
                : entity.getGroupIds().stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                directPermissions,
                groupIds
        );
    }

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());

        entity.setDirectPermissions(user.getDirectPermissions());
        entity.setGroupIds(
                user.getGroupIds().stream()
                        .map(UUID::toString)
                        .collect(Collectors.toSet())
        );
        return entity;
    }
}
