package me.sarah.permissionservice.adapter.out.mongo;

import me.sarah.permissionservice.adapter.out.mongo.entity.UserEntity;
import me.sarah.permissionservice.adapter.out.mongo.mapper.UserPersistenceMapper;
import me.sarah.permissionservice.adapter.out.mongo.repository.MongoUserRepository;
import me.sarah.permissionservice.domain.model.User;
import me.sarah.permissionservice.port.out.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final MongoUserRepository mongoUserRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public UserRepositoryAdapter(MongoUserRepository mongoUserRepository, UserPersistenceMapper userPersistenceMapper) {
        this.mongoUserRepository = mongoUserRepository;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return mongoUserRepository.findById(userId)
                .map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return mongoUserRepository.findByUsername(username)
                .map(userPersistenceMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userPersistenceMapper.toEntity(user);
        UserEntity savedUserEntity = mongoUserRepository.save(userEntity);
        return userPersistenceMapper.toDomain(savedUserEntity);
    }
}
