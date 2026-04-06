package me.sarah.permissionservice.adapter.in.rest.mapper;

import me.sarah.permissionservice.adapter.in.rest.dto.UserResponse;
import me.sarah.permissionservice.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserWebMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
