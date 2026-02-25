
package me.sarah.permissionservice.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class User {

    private UUID id;
    private String username;
    private Set<String> permissions;
    private Set<Group> groups = new HashSet<>();

    public User(UUID id, String username) {
        this.id = id;
        this.username = username;
        this.permissions = permissions;
        this.groups = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public Set<String> getEffectivePermissions() {
        return Set.of();
    }
}