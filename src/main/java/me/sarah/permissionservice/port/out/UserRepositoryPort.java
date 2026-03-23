package me.sarah.permissionservice.port.out;

import me.sarah.permissionservice.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    Optional<User> findById(UUID userId);

    Optional<User> findByUsername(String username);

    User save(User user);
}
