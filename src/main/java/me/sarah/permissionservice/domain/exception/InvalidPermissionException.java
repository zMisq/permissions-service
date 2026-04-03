package me.sarah.permissionservice.domain.exception;

public class InvalidPermissionException extends DomainException{

    public InvalidPermissionException(String permission) {
        super("Invalid permission: " + permission);
    }
}
