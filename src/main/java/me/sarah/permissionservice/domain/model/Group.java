package me.sarah.permissionservice.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class Group {

    private UUID id;
    private String name;
    private Set<String> permissions = new HashSet<>();
    private Set<User> users = new HashSet<>();

    public Group(UUID id, String name) {
      this.id = id;
      this.name = name;
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<User> getUsers() {
        return users;
    }
}
