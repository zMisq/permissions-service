
package me.sarah.permissionservice.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class User {

    private final UUID id;
    private final String username;
    private final Set<String> permissions = new HashSet<>();
    private final Set<Group> groups = new HashSet<>();

    public User(UUID id, String username) {
        this.id = id;
        this.username = username;
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
        final Set<String> effectivePermissions = new HashSet<>(permissions);
        groups.stream().map(Group::getPermissions).forEach(effectivePermissions::addAll);
        return effectivePermissions;
    }
}