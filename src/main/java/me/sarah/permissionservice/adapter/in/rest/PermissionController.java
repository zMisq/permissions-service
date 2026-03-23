package me.sarah.permissionservice.adapter.in.rest;

import me.sarah.permissionservice.adapter.in.rest.dto.PermissionCheckResponse;
import me.sarah.permissionservice.port.in.PermissionUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/users/{userId}/permissions")
public class PermissionController {

    private final PermissionUseCase permissionUseCase;

    public PermissionController(PermissionUseCase permissionUseCase) {
        this.permissionUseCase = permissionUseCase;
    }

    @GetMapping("/check")
    public PermissionCheckResponse hasPermission(@PathVariable UUID userId, @RequestParam String permission) {
        boolean hasPermission = permissionUseCase.hasPermission(userId, permission);

        return new PermissionCheckResponse(
                userId,
                permission,
                hasPermission
        );
    }

}
