package me.sarah.permissionservice.adapter.out.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Document("users")
public class UserEntity {

    @Id
    private UUID id;
    private String username;
    private String email;
    private Set<String> directPermissions;
    private Set<String> groupIds;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Set<String> getDirectPermissions() {
        return directPermissions;
    }
    public void setDirectPermissions(Set<String>directPermissions) {
        this.directPermissions = directPermissions;
    }
    public Set<String> getGroupIds() {
        return groupIds;
    }
    public void setGroupIds(Set<String> groupIds) {
        this.groupIds = groupIds;
    }

}
