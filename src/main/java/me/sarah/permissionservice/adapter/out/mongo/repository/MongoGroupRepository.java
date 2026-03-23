package me.sarah.permissionservice.adapter.out.mongo.repository;

import me.sarah.permissionservice.adapter.out.mongo.entity.GroupEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface MongoGroupRepository extends MongoRepository<GroupEntity, UUID> {

    Optional<GroupEntity> findByName(String name);
}
