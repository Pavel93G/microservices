package ru.itm.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itm.app.dto.UserDTO;
import ru.itm.app.mapper.UserMapper;
import ru.itm.app.models.User;
import ru.itm.app.repositories.UserRepository;
import ru.itm.app.security.UserDetailsImp;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(RoleService roleService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return UserMapper.toDTOList(userRepository.findAll());
    }

    @Transactional
    @Override
    public UserDTO findUserById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.map(UserMapper::toDTO).orElse(null);
    }

    @Override
    public Optional<UserDTO> findUserByUsername(String username) {
        return userRepository.findByUsername(username).map(UserMapper::toDTO);
    }

    @Override
    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(UserMapper::toDTO);
    }

    @Override
    public Optional<UserDTO> findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).map(UserMapper::toDTO);
    }

    @Transactional()
    @Override
    public void addUser(UserDTO userDTO) {
        var user = UserMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional()
    @Override
    public void updateUser(Long id, UserDTO updateUserDTO) {
        var updateUser = UserMapper.toUser(updateUserDTO);
        updateUser.setId(id);
        updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        userRepository.save(updateUser);
    }

    @Transactional()
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Пользователь '%s' не найден", username)));

        return new UserDetailsImp(user);
    }
}
