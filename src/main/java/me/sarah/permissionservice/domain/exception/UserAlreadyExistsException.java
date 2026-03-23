package me.sarah.permissionservice.domain.exception;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String username) {
        super("User already exists: " + username);
    }
}
