package ru.itm.app.dao;

import ru.itm.app.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> findAllUsers();
    Optional<User> findUserById(Long id);
    void addUser(User user);
    void updateUser(User updateUser);
    void deleteUser(Long id);
}
