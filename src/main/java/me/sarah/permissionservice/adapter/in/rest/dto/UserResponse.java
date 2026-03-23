package me.sarah.permissionservice.adapter.in.rest.dto;

import java.util.UUID;

public record UserResponse (UUID id, String username, String email) {
}
