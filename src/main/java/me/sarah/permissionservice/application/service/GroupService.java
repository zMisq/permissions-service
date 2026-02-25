package me.sarah.permissionservice.application.service;

import me.sarah.permissionservice.domain.model.Group;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    Group createGroup(String name);

    Group getGroup(UUID id);

    List<Group> getAllGroups();

    boolean deleteGroup(UUID id);

    boolean addUserToGroup(UUID userid, UUID groupId);
}
