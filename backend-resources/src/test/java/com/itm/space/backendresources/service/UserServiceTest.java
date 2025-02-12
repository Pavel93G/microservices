package com.itm.space.backendresources.service;

import com.itm.space.backendresources.BaseIntegrationTest;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.exception.BackendResourcesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest extends BaseIntegrationTest {

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    Keycloak keycloakClient;

    @Autowired
    UserService userService;

    @Value("${keycloak.realm}")
    private String realm;

    @Test
    @DisplayName("createUser() - успешное создание пользователя")
    void creatUser_whenValidRequest_thenSuccess() {
        // Given
        UserRequest userRequest = creatTestUserRequest();

        Response response = Response.status(CREATED)
                .header("Location", "http://localhost/auth/admin/realms/" + realm + "/users/12345").build();
        when(keycloakClient.realm(realm).users().create(any(UserRepresentation.class)))
                .thenReturn(response);

        // When & Then
        assertDoesNotThrow(() -> userService.createUser(userRequest));


        verify(keycloakClient.realm(anyString()).users(), times(1))
                .create(any(UserRepresentation.class));
    }

    @Test
    @DisplayName("createUser() - BackendResourcesException при ошибке Keycloak")
    void creatUser_whenKeycloackFails_thenThrowsBackendResourcesException() {
        // given
        UserRequest userRequest = creatTestUserRequest();

        // when & then
        Response errorResponse = Response.status(Response.Status.BAD_REQUEST).build();
        var keycloakException = new WebApplicationException("Keycloak creation error", errorResponse);
        when(keycloakClient.realm(realm).users().create(any(UserRepresentation.class)))
                .thenThrow(keycloakException);

        assertThrows(
                BackendResourcesException.class,
                () -> userService.createUser(userRequest)
        );
    }

    @Test
    @DisplayName("getUserById() - успешное получение данных пользователя")
    void getUserById_whenUserExists() {
        //given
        UUID id = UUID.randomUUID();
        String idStr = String.valueOf(id);

        UserRepresentation userRepresentation = creatTestUserRepresentation();
        List<RoleRepresentation> roles = creatTestRoles();
        List<GroupRepresentation> groups = createTestGroups();

        var userResource = keycloakClient.realm(realm).users().get(idStr);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);
        when(userResource.roles().getAll().getRealmMappings()).thenReturn(roles);
        when(userResource.groups()).thenReturn(groups);

        //when
        var userResponse = userService.getUserById(id);

        //then
        assertNotNull(userResponse);
        assertEquals("User", userResponse.getLastName());
        assertEquals("Userov", userResponse.getFirstName());
        assertEquals("user@mail.com", userResponse.getEmail());
        assertIterableEquals(List.of("MODERATOR"),
                roles.stream().map(RoleRepresentation::getName).toList());
        assertIterableEquals(List.of("Moderators"),
                groups.stream().map(GroupRepresentation::getName).toList());
    }

    @Test
    @DisplayName("getUserById() - BackendResourcesException при ошибке Keycloak")
    void getUserById_whenKeycloakFails_thenThrowsBackendResourcesException() {
        //given
        UUID id = UUID.randomUUID();
        String idStr = String.valueOf(id);

        when(keycloakClient.realm(realm).users().get(idStr).toRepresentation())
                .thenThrow(new RuntimeException("Keycloack error"));

        //when & then
        var backendResourcesException = assertThrows(
                BackendResourcesException.class,
                () -> userService.getUserById(id)
        );

        assertTrue(backendResourcesException.getMessage().contains("Keycloack error"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, backendResourcesException.getHttpStatus());
    }

    private UserRepresentation creatTestUserRepresentation() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("User123");
        userRepresentation.setEmail("user@mail.com");
        userRepresentation.setLastName("User");
        userRepresentation.setFirstName("Userov");

        return userRepresentation;
    }

    private List<RoleRepresentation> creatTestRoles() {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName("MODERATOR");

        return List.of(roleRepresentation);
    }

    private List<GroupRepresentation> createTestGroups() {
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName("Moderators");

        return List.of(groupRepresentation);
    }

    private UserRequest creatTestUserRequest() {
        return new UserRequest(
                "User123",
                "user@mail.com",
                "password",
                "User",
                "Userov"
        );
    }

}
