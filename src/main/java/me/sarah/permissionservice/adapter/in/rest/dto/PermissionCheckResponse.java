package me.sarah.permissionservice.adapter.in.rest.dto;

import java.util.UUID;

public record PermissionCheckResponse(
        UUID userId,
        String permission,
        boolean hasPermission
) {
}
