package me.sarah.permissionservice.domain.exception;

public class GroupAlreadyExistsException extends DomainException {

    public GroupAlreadyExistsException(String name) {
        super("Group already exists: " + name);
    }
}
