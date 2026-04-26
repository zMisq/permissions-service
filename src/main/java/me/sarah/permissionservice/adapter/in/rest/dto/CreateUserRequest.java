package me.sarah.permissionservice.adapter.in.rest.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String username,
        @Email @NotBlank String email
) {
}
