package me.sarah.permissionservice.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Group {

    private final UUID id;
    private final String name;
    private final String description;
    private final Set<String> permissions;

    public Group(UUID id, String name, String description, Set<String> permissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = new HashSet<>(permissions);
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getPermissions() {
        return new HashSet<>(permissions);
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }
}
