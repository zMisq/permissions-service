package me.sarah.permissionservice.adapter.out.mongo.mapper;

import me.sarah.permissionservice.adapter.out.mongo.entity.UserEntity;
import me.sarah.permissionservice.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserPersistenceMapper {

    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                new HashSet<>(),
                new HashSet<>()
        );
    }

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setDirectPermissions(user.getDirectPermissions());
        return entity;
    }
}
