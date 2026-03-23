package me.sarah.permissionservice.domain.exception;

import java.util.UUID;

public class GroupNotFoundException extends DomainException{

    public GroupNotFoundException(UUID groupId) {
        super("Group not found: " + groupId);
    }
}
