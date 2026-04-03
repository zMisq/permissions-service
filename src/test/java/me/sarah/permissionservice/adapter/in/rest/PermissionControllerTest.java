package me.sarah.permissionservice.adapter.in.rest;

import me.sarah.permissionservice.adapter.in.rest.mapper.UserWebMapper;
import me.sarah.permissionservice.domain.exception.InvalidPermissionException;
import me.sarah.permissionservice.port.in.PermissionUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PermissionController.class)
class PermissionControllerTest {

    private static final UUID USER_ID = UUID.fromString("e49e08b3-b970-48c4-9d2b-1b5b4881fd6d");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PermissionUseCase permissionUseCase;

    @MockitoBean
    private UserWebMapper userWebMapper;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void Test_should_get_Permissions(boolean hasPermission) throws Exception {
        UUID userId = UUID.fromString("e49e08b3-b970-48c4-9d2b-1b5b4881fd6d");

        when(permissionUseCase.hasPermission(USER_ID, "READ"))
                .thenReturn(hasPermission);

        mockMvc.perform(
                        get("/users/{userId}/permissions/check", userId)
                                .param("permission", "READ")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.permission").value("READ"))
                .andExpect(jsonPath("$.hasPermission").value(hasPermission + ""));
    }

    @Test
    void Test_should_get_InvalidPermission() throws Exception {
        var exception = new InvalidPermissionException("READ");

        when(permissionUseCase.hasPermission(USER_ID, "READ"))
                .thenThrow(exception);

        mockMvc.perform(get("/users/{userId}/permissions/check", USER_ID)
                        .param("permission", "READ"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid permission"))
                .andExpect(jsonPath("$.detail")
                        .value("Invalid permission: READ"));

    }
}