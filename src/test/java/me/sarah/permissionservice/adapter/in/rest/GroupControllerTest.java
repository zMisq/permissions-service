package me.sarah.permissionservice.adapter.in.rest;

import me.sarah.permissionservice.adapter.in.rest.dto.GroupResponse;
import me.sarah.permissionservice.adapter.in.rest.mapper.GroupWebMapper;
import me.sarah.permissionservice.domain.exception.GroupAlreadyExistsException;
import me.sarah.permissionservice.domain.exception.GroupNotFoundException;
import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.port.in.GroupUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GroupController.class)
@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    private static final UUID GROUP_ID = UUID.fromString("e49e08b3-b970-48c4-9d2b-1b5b4881fd6d");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupUseCase groupUseCase;

    @MockitoBean
    private GroupWebMapper groupWebMapper;

    @Mock
    private Group mockedGroup;

    @Test
    void Test_should_create_Group() throws Exception {
        GroupResponse response = new GroupResponse(
                GROUP_ID,
                "someGroup",
                "someDescription",
                Set.of()
        );

        when(groupUseCase.createGroup("someGroup",
                "someDescription"))
                .thenReturn(mockedGroup);
        when(groupWebMapper.toResponse(mockedGroup))
                .thenReturn(response);

        var result = mockMvc.perform(post("/groups")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                          "name": "someGroup",
                          "description": "someDescription"
                        }
                        """));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GROUP_ID.toString()))
                .andExpect(jsonPath("$.name").value("someGroup"))
                .andExpect(jsonPath("$.description").value("someDescription"));
    }

    @Test
    void Test_should_get_Group_by_id() throws Exception {
        GroupResponse response = new GroupResponse(
                GROUP_ID,
                "someGroup",
                "someDescription",
                Set.of()
        );

        when(groupUseCase.getGroupById(GROUP_ID))
                .thenReturn(mockedGroup);
        when(groupWebMapper.toResponse(mockedGroup))
                .thenReturn(response);

        mockMvc.perform(get("/groups/{groupId}", GROUP_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GROUP_ID.toString()))
                .andExpect(jsonPath("$.name").value("someGroup"))
                .andExpect(jsonPath("$.description").value("someDescription"));
    }

    @Test
    void Test_should_assignPermission() throws Exception {
        GroupResponse response = new GroupResponse(
                GROUP_ID,
                "someGroup",
                "someDescription",
                Set.of("READ")
        );

        when(groupUseCase.assignPermission(GROUP_ID, "READ"))
                .thenReturn(mockedGroup);
        when(groupWebMapper.toResponse(mockedGroup))
                .thenReturn(response);

        mockMvc.perform(post("/groups/{groupId}/permissions", GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "permission": "READ"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GROUP_ID.toString()))
                .andExpect(jsonPath("$.name").value("someGroup"))
                .andExpect(jsonPath("$.description").value("someDescription"))
                .andExpect(jsonPath("$.permissions[0]").value("READ"));

    }

    @Test
    void Test_should_removePermission() throws Exception {
        GroupResponse response = new GroupResponse(
                GROUP_ID,
                "someGroup",
                "someDescription",
                Set.of()
        );

        when(groupUseCase.removePermission(GROUP_ID, "toBeDeleted"))
                .thenReturn(mockedGroup);
        when(groupWebMapper.toResponse(mockedGroup))
                .thenReturn(response);

        mockMvc.perform(delete("/groups/{groupId}/permissions", GROUP_ID)
                        .param("permission", "toBeDeleted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GROUP_ID.toString()))
                .andExpect(jsonPath("$.name").value("someGroup"))
                .andExpect(jsonPath("$.description").value("someDescription"));
    }

    @Test
    void Test_should_get_GroupNotFoundException() throws Exception {
        var exception = new GroupNotFoundException(GROUP_ID);

        when(groupUseCase.getGroupById(GROUP_ID))
                .thenThrow(exception);

        mockMvc.perform(get("/groups/{groupId}", GROUP_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Group not found"))
                .andExpect(jsonPath("$.detail")
                        .value("Group not found: " + GROUP_ID));
    }

    @Test
    void Test_should_get_GroupAlreadyExistsException() throws Exception {
        var exception = new GroupAlreadyExistsException("someGroup");

        when(groupUseCase.createGroup("someGroup", "someDescription"))
                .thenThrow(exception);

        mockMvc.perform(post("/groups")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "someGroup",
                                  "description": "someDescription"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Group already exists"))
                .andExpect(jsonPath("$.detail")
                        .value("Group already exists: someGroup"));
    }
}