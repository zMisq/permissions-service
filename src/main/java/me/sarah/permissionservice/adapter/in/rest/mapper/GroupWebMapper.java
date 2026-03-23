package me.sarah.permissionservice.adapter.in.rest.mapper;

import me.sarah.permissionservice.adapter.in.rest.dto.GroupResponse;
import me.sarah.permissionservice.domain.model.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupWebMapper {

    public GroupResponse toResponse(Group group) {
        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getPermissions()
        );
    }
}
