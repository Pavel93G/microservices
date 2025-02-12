package com.itm.space.backendresources.controller;

import com.itm.space.backendresources.BaseIntegrationTest;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.exception.BackendResourcesException;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class UserControllerTest extends BaseIntegrationTest {

    @MockBean
    UserService userService;

    @Test
    @DisplayName("Создание пользователя — 200 OK при валидном запросе")
    @WithMockUser(roles = "MODERATOR")
    void createUser_whenValidInput_thenReturns200() throws Exception {
        //given
        UserRequest userRequest = new UserRequest(
                "User123",
                "user@mail.com",
                "password",
                "User",
                "Userov"
        );

        doNothing().when(userService).createUser(userRequest);

        //when
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                //then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Создание пользователя — 400 Bad Request при невалидном email")
    @WithMockUser(roles = "MODERATOR")
    void creatUser_whenInvalidEmail_thenReturn400() throws Exception {
        //given
        UserRequest userRequest = new UserRequest(
                "User123",
                "invalid-email",
                "password",
                "User",
                "User"
        );

        doNothing().when(userService).createUser(userRequest);

        //when
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                //then
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.email").value("Email should be valid")
                );
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void getUserById_whenUserExists_thenReturnsUserResponse() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(
                "User",
                "Userov",
                "user@mail.com",
                Arrays.asList("MODERATOR"),
                Arrays.asList("Moderators")
        );

        //when
        when(userService.getUserById(id)).thenReturn(userResponse);

        mvc.perform(get("/api/users/{id}", id))
                //then
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.firstName").value("User"),
                        jsonPath("$.lastName").value("Userov"),
                        jsonPath("$.email").value("user@mail.com"),
                        jsonPath("$.roles").isArray(),
                        jsonPath("$.roles[0]").value("MODERATOR"),
                        jsonPath("$.groups").isArray(),
                        jsonPath("$.groups[0]").value("Moderators")
                );
    }

    @Test
    @DisplayName("Получение пользователя — 404 Not Found если пользователь не найден")
    @WithMockUser(roles = "MODERATOR")
    void getUserById_whenUserNotFound_thenReturn404() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        when(userService.getUserById(id))
                .thenThrow(new BackendResourcesException("User not found", HttpStatus.NOT_FOUND));

        //when
        mvc.perform(get("/api/users/{id}", id))
                //then
                .andExpectAll(
                        status().isNotFound(),
                        content().string("User not found")
                );
    }

    @Test
    @DisplayName("Получение имени пользователя — 200 OK")
    @WithMockUser(roles = "MODERATOR", username = "Username")
    void helloEndpoint_whenCalled_thenReturnsUserName() throws Exception {
        mvc.perform(get("/api/users/hello"))
                .andExpectAll(
                        status().isOk(),
                        content().string("Username")
                );
    }

    @Test
    @DisplayName("Запрос без авторизации — 403 Forbidden")
    void getUserById_whenNotAuthenticated_thenReturns403() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(get("/api/users/" + id))
                .andExpect(status().isUnauthorized());
    }
}
