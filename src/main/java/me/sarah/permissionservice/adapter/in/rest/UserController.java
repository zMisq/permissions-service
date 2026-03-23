package me.sarah.permissionservice.adapter.in.rest;

import me.sarah.permissionservice.adapter.in.rest.dto.CreateUserRequest;
import me.sarah.permissionservice.adapter.in.rest.dto.UserResponse;
import me.sarah.permissionservice.adapter.in.rest.mapper.UserWebMapper;
import me.sarah.permissionservice.domain.model.User;
import me.sarah.permissionservice.port.in.UserUseCase;
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
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        User user = userUseCase.createUser(request.username(),
                request.email());
                return userWebMapper.toResponse(user);
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable UUID userId) {
        User user = userUseCase.getUserById(userId);
        return userWebMapper.toResponse(user);
    }

}