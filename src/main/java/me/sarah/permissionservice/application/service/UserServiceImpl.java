package me.sarah.permissionservice.application.service;

import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public User createUser(String username) {
        User user = new User(UUID.randomUUID(), username);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(UUID id) {
        return users.get(id);
    }

    @Override
    public void addPermissionToUser(UUID userId, String permission) {
        User user = users.get(userId);

        user.addPermission(permission);
    }

    @Override
    public Set<String> getEffectivePermissions(UUID userId) {
        User user = users.get(userId);

        if (user == null) {
            return new HashSet<>();
            //Leer fals User ned existiert
        }
        return user.getEffectivePermissions();
    }

    @Override
    public void addUserToGroup(UUID userId, Group group) {
        User user = users.get(userId);
        user.addGroup(group);
    }
}
