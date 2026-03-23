package me.sarah.permissionservice.domain.service;

import me.sarah.permissionservice.domain.exception.GroupNotFoundException;
import me.sarah.permissionservice.domain.exception.UserAlreadyExistsException;
import me.sarah.permissionservice.domain.exception.UserNotFoundException;
import me.sarah.permissionservice.domain.model.User;
import me.sarah.permissionservice.port.in.UserUseCase;
import me.sarah.permissionservice.port.out.GroupRepositoryPort;
import me.sarah.permissionservice.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final GroupRepositoryPort groupRepositoryPort;
    private final UserCreator userCreator;

    public UserService(UserRepositoryPort userRepositoryPort, GroupRepositoryPort groupRepositoryPort, UserCreator userCreator) {
        this.userRepositoryPort = userRepositoryPort;
        this.groupRepositoryPort = groupRepositoryPort;
        this.userCreator = userCreator;
    }

    @Override
    public User createUser(String username, String email) {
        userRepositoryPort.findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException(user.getUsername());
                });

        User user = userCreator.create(UUID.randomUUID(), username, email);
        return userRepositoryPort.save(user);
    }

    @Override
    public User getUserById(UUID userId) {
        return userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public User addUserToGroup(UUID userId, UUID groupId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        groupRepositoryPort.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        user.addGroup(groupId);
        return userRepositoryPort.save(user);
    }

    @Override
    public User removeUserFromGroup(UUID userId, UUID groupId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.removeGroup(groupId);
        return userRepositoryPort.save(user);
    }
}
