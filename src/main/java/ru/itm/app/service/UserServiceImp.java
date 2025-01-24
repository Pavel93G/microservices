package ru.itm.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itm.app.models.User;
import ru.itm.app.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElse(null);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional()
    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Transactional()
    @Override
    public void updateUser(Long id, User updateUser) {
        updateUser.setId(id);
        userRepository.save(updateUser);
    }

    @Transactional()
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public String getCurrentUserPhoneNumber(User user) {
        var cuerrentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        return cuerrentUser.getPhoneNumber();
    }

    @Override
    public String getCurrentUserEmail(User user) {
        var currentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        return currentUser.getEmail();
    }
}
