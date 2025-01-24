package ru.itm.app.service;

import ru.itm.app.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    User findUserById(Long id);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByPhoneNumber(String phoneNumber);
    void addUser(User user);
    void updateUser(Long id, User updateUser);
    void deleteUser(Long id);
    String getCurrentUserEmail(User user);
    String getCurrentUserPhoneNumber(User user);
}
