package ru.itm.app.service;

import org.springframework.web.client.RestTemplate;
import ru.itm.app.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
    User addUser();
    User updateUser(Long id);
    void deletedUser(Long id);
}
