package me.sarah.permissionservice.adapter.in.rest;


import me.sarah.permissionservice.adapter.in.rest.dto.UserResponse;
import me.sarah.permissionservice.adapter.in.rest.mapper.UserWebMapper;
import me.sarah.permissionservice.domain.exception.DomainException;
import me.sarah.permissionservice.domain.exception.UserAlreadyExistsException;
import me.sarah.permissionservice.domain.exception.UserNotFoundException;
import me.sarah.permissionservice.domain.model.User;
import me.sarah.permissionservice.port.in.UserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final UUID USER_ID = UUID.fromString("e49e08b3-b970-48c4-9d2b-1b5b4881fd6d");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private UserWebMapper userWebMapper;

    @Test
    void Test_should_create_user(@Mock User mockedUser) throws Exception {
        UserResponse response = new UserResponse(
                USER_ID,
                "someUser",
                "someUser@example.com"
        );

        when(userUseCase.createUser("someUser", "someUser@example.com"))
                .thenReturn(mockedUser);
        when(userWebMapper.toResponse(mockedUser))
                .thenReturn(response);

        var result = mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                          "username": "someUser",
                          "email": "someUser@example.com"
                        }
                        """));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID.toString()))
                .andExpect(jsonPath("$.username").value("someUser"))
                .andExpect(jsonPath("$.email").value("someUser@example.com"));
    }

    @Test
    void Test_should_get_user_by_id(@Mock User mockedUser) throws Exception {
        UserResponse response = new UserResponse(
                USER_ID,
                "someUser",
                "someUser@example.com"
        );

        when(userUseCase.getUserById(USER_ID))
                .thenReturn(mockedUser);
        when(userWebMapper.toResponse(mockedUser))
                .thenReturn(response);

        mockMvc.perform(get("/users/{userId}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID.toString()))
                .andExpect(jsonPath("$.username").value("someUser"))
                .andExpect(jsonPath("$.email").value("someUser@example.com"));
    }


    @Test
    void Test_should_get_UserNotFoundException() throws Exception {
        var exception = new UserNotFoundException(USER_ID);

        when(userUseCase.getUserById(USER_ID))
                .thenThrow(exception);

        mockMvc.perform(get("/users/{userId}", USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("User not found"))
                .andExpect(jsonPath("$.detail")
                        .value("User not found: e49e08b3-b970-48c4-9d2b-1b5b4881fd6d"));
    }

    @Test
    void Test_should_get_UserAlreadyExistsException() throws Exception {
        var exception = new UserAlreadyExistsException("someUser");

        when(userUseCase.createUser("someUser", "some@mail"))
                .thenThrow(exception);

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "someUser",
                                  "email": "some@mail"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("User already exists"))
                .andExpect(jsonPath("$.detail")
                        .value("User already exists: someUser"));
    }

    @Test
    void Test_should_get_DomainException() throws Exception {
        var exception = new DomainException("Domain error");

        when(userUseCase.getUserById(USER_ID))
                .thenThrow(exception);

        mockMvc.perform(get("/users/{userId}", USER_ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Domain error"))
                .andExpect(jsonPath("$.detail")
                        .value("Domain error"));
    }
}
