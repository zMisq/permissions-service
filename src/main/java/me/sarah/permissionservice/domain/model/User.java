
package me.sarah.permissionservice.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {

    private final UUID id;
    private final String username;
    private final String email;
    private final Set<String> directPermissions;
    private final Set<UUID> groupIds;

    public User(UUID id, String username, String email, Set<String> directPermissions, Set<UUID> groupIds) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.directPermissions = new HashSet<>(directPermissions);
        this.groupIds = new HashSet<>(groupIds);
    }


    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getDirectPermissions() {
        return new HashSet<>(directPermissions);
    }

    public Set<UUID> getGroupIds() {
        return new HashSet<>(groupIds);
    }

    public void addPermission(String permission) {
        directPermissions.add(permission);
    }

    public void removePermission(String permission) {
        directPermissions.remove(permission);
    }

    public void addGroup(UUID groupId) {
        groupIds.add(groupId);
    }

    public void removeGroup(UUID groupId) {
        groupIds.remove(groupId);
    }
}
