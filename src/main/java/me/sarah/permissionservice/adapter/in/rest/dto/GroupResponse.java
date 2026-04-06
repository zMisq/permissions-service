package me.sarah.permissionservice.adapter.in.rest.dto;

import java.util.Set;
import java.util.UUID;

public record GroupResponse(UUID id, String name, String description, Set<String> permissions) {
}
