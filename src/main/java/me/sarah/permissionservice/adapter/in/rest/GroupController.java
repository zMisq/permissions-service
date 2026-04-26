package me.sarah.permissionservice.adapter.in.rest;

import jakarta.validation.Valid;
import me.sarah.permissionservice.adapter.in.rest.dto.AssignPermissionRequest;
import me.sarah.permissionservice.adapter.in.rest.dto.CreateGroupRequest;
import me.sarah.permissionservice.adapter.in.rest.dto.GroupResponse;
import me.sarah.permissionservice.adapter.in.rest.mapper.GroupWebMapper;
import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.port.in.GroupUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupUseCase groupUseCase;
    private final GroupWebMapper groupWebMapper;

    public GroupController(GroupUseCase groupUseCase, GroupWebMapper groupWebMapper) {
        this.groupUseCase = groupUseCase;
        this.groupWebMapper = groupWebMapper;
    }

    @PostMapping
    public GroupResponse createGroup(@Valid @RequestBody CreateGroupRequest request) {
        Group group = groupUseCase.createGroup(request.name(),
                request.description());
        return groupWebMapper.toResponse(group);
    }

    @GetMapping("/{groupId}")
    public GroupResponse getGroupById(@PathVariable UUID groupId) {
        Group group = groupUseCase.getGroupById(groupId);
        return groupWebMapper.toResponse(group);
    }

    @PostMapping("/{groupId}/permissions")
    public GroupResponse assignPermission(@PathVariable UUID groupId, @RequestBody AssignPermissionRequest request) {
        Group group = groupUseCase.assignPermission(groupId, request.permission());
        return groupWebMapper.toResponse(group);
    }

    @DeleteMapping("/{groupId}/permissions")
    public GroupResponse removePermission(@PathVariable UUID groupId, @RequestParam String permission) {
        Group group = groupUseCase.removePermission(groupId, permission);
        return groupWebMapper.toResponse(group);
    }
}
