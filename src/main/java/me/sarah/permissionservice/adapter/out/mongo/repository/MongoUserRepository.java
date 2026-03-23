package me.sarah.permissionservice.adapter.out.mongo.repository;

import me.sarah.permissionservice.adapter.out.mongo.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface MongoUserRepository extends MongoRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
}
