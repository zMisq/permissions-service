package me.sarah.permissionservice.adapter.in.rest;

import me.sarah.permissionservice.adapter.in.rest.dto.CreateUserRequest;
import me.sarah.permissionservice.adapter.in.rest.dto.UserResponse;
import me.sarah.permissionservice.adapter.in.rest.mapper.UserWebMapper;
import me.sarah.permissionservice.domain.model.User;
import me.sarah.permissionservice.port.in.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserWebMapper userWebMapper;

    public UserController(UserUseCase userUseCase, UserWebMapper userWebMapper) {
        this.userUseCase = userUseCase;
        this.userWebMapper = userWebMapper;
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userUseCase.createUser(request.username(),
                request.email());
        return userWebMapper.toResponse(user);
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable UUID userId) {
        User user = userUseCase.getUserById(userId);
        return userWebMapper.toResponse(user);
    }

    @PostMapping("/{userId}/groups/{groupId}")
    public UserResponse addUserToGroup(@PathVariable UUID userId, @PathVariable UUID groupId) {
        User user = userUseCase.addUserToGroup(userId, groupId);
        return userWebMapper.toResponse(user);
    }

    @DeleteMapping("/{userId}/groups/{groupId}")
    public UserResponse removeUserFromGroup(@PathVariable UUID userId, @PathVariable UUID groupId) {
        User user = userUseCase.removeUserFromGroup(userId, groupId);
        return userWebMapper.toResponse(user);
    }

}
