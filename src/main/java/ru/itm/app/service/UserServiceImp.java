package ru.itm.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itm.app.config.RestTemplateConfig;
import ru.itm.app.model.User;

import java.util.List;

@Service
public class UserServiceImp implements UserService{
    private final RestTemplate restTemplate;
    private final RestTemplateConfig restTemplateConfig;
    private HttpHeaders headers = new HttpHeaders();
    private String sessionId;

    @Autowired
    public UserServiceImp(RestTemplate restTemplate, RestTemplateConfig restTemplateConfig) {
        this.restTemplate = restTemplate;
        this.restTemplateConfig = restTemplateConfig;
    }

    @Override
    public List<User> findAllUsers() {
        if (sessionId != null) {
            headers.set(HttpHeaders.COOKIE, sessionId);
        }
        ResponseEntity<List<User>> response = restTemplate.exchange(
                restTemplateConfig.getApiUrl(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<User>>() {}
        );

        sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        System.out.println("Session ID: " + sessionId);

        return response.getBody();
    }

    @Override
    public User addUser() {
        User user = new User("James", "Brown", (byte) 30);

        if (sessionId != null) {
            headers.set(HttpHeaders.COOKIE, sessionId);
        }

        ResponseEntity<User> response = restTemplate.exchange(
                restTemplateConfig.getApiUrl(),
                HttpMethod.POST,
                new HttpEntity<>(user, headers),
                User.class
        );
        System.out.println("Session ID: " + sessionId);

        return response.getBody();
    }

    @Override
    public User updateUser(Long id) {
        User user = addUser();
        user.setName("Thomas");
        user.setLastname("Shelby");

        headers.set(HttpHeaders.COOKIE, sessionId);

        ResponseEntity<User> response = restTemplate.exchange(
                restTemplateConfig.getApiUrl(),
                HttpMethod.PUT,
                new HttpEntity<>(user, headers),
                User.class
        );

        return response.getBody();
    }

    @Override
    public void deletedUser(Long id) {
        restTemplate.exchange(
                restTemplateConfig.getApiUrl() + "/" + id,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );
    }
}
