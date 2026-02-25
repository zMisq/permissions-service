package me.sarah.permissionservice.application.service;

import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.domain.model.User;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {

    private final Map<String, Group>
    groups = new HashMap<>();
    private final UserService userService;

    public GroupServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Group createGroup(String name){
        Group group = new Group(UUID.randomUUID(), name);
        groups.put(name, group);
        return group;
    }

    @Override
    public List<Group> getAllGroups()
    {
        return new ArrayList<>(groups.values());
    }

    @Override
    public Group getGroup(UUID id) {
        return groups.get(id);
    }

    @Override
    public boolean deleteGroup(UUID id) {
        return groups.remove(id) != null;
    }

    @Override
    public boolean addUserToGroup(UUID userId, UUID groupId) {
        Group group = groups.get(groupId);
        User user = userService.getUser(userId);
        if (group != null && user != null) {
            group.getUsers().add(user);
            return true;
        }
        return false;
    }
}
