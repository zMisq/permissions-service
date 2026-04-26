package me.sarah.permissionservice.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateGroupRequest(
        @NotBlank String name,
        @NotBlank String description
) {
}
