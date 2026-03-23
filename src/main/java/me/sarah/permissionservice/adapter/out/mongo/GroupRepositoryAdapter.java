package me.sarah.permissionservice.adapter.out.mongo;

import me.sarah.permissionservice.adapter.out.mongo.entity.GroupEntity;
import me.sarah.permissionservice.adapter.out.mongo.mapper.GroupPersistenceMapper;
import me.sarah.permissionservice.adapter.out.mongo.repository.MongoGroupRepository;
import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.port.out.GroupRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class GroupRepositoryAdapter implements GroupRepositoryPort {

    private final MongoGroupRepository mongoGroupRepository;
    private final GroupPersistenceMapper groupPersistenceMapper;

    public GroupRepositoryAdapter(MongoGroupRepository mongoGroupRepository, GroupPersistenceMapper groupPersistenceMapper) {
        this.mongoGroupRepository = mongoGroupRepository;
        this.groupPersistenceMapper = groupPersistenceMapper;
    }

    @Override
    public Optional<Group> findById(UUID groupId) {
        return mongoGroupRepository.findById(groupId)
                .map(groupPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Group> findByName(String name) {
        return mongoGroupRepository.findByName(name)
                .map(groupPersistenceMapper::toDomain);
    }

    @Override
    public Group save(Group group) {
        GroupEntity groupEntity = groupPersistenceMapper.toEntity(group);
        GroupEntity savedGroupEntity = mongoGroupRepository.save(groupEntity);

        return groupPersistenceMapper.toDomain(savedGroupEntity);
    }
}
