package ru.itm.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itm.app.config.RestTemplateConfig;
import ru.itm.app.model.User;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImp implements UserService{
    private final RestTemplate restTemplate;
    private HttpHeaders headers = new HttpHeaders();
    private String sessionId;

    @Value("${api.url}")
    private String url;

    @Autowired
    public UserServiceImp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<User> findAllUsers() {
        ResponseEntity<User[]> response = restTemplate.getForEntity(url, User[].class);

        sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        System.out.println("Session ID: " + sessionId);
        headers.set("Cookie", sessionId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var userList = Arrays.asList(response.getBody());
        userList.stream().forEach(System.out::println);

        return userList;
    }

    @Override
    public String addUser() {
        User newUser = new User(3L,"James", "Brown", (byte) 20);

        HttpEntity<User> request = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getBody();
    }

    @Override
    public String updateUser() {
        User updateUser = new User(3L, "Emma", "Smith", (byte) 21);

        HttpEntity<User> request = new HttpEntity<>(updateUser, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                String.class
        );

        return response.getBody();
    }

    @Override
    public String deletedUser() {
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url + "/3",
                HttpMethod.DELETE,
                request,
                String.class
        );

        return response.getBody();
    }
}
