package ru.itm.app.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itm.app.api.dto.UserDTO;
import ru.itm.app.api.exceptions.UserNotCreatedException;
import ru.itm.app.api.mapper.UserMapper;
import ru.itm.app.storage.models.User;
import ru.itm.app.storage.repositories.UserRepository;
import ru.itm.app.api.security.UserDetailsImp;
import ru.itm.app.api.exceptions.UserNotFoundException;

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

        return foundUser.map(UserMapper::toDTO)
                .orElseThrow(() ->
                        new UserNotFoundException("Пользователь с ID: " + id + " не найден!"));
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
    public UserDTO addUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserNotCreatedException("Пользователь с таким Username: " + userDTO.getUsername() +" уже существует!");
        }

        if (userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent()) {
            throw new UserNotCreatedException("Пользователь с таким номером телефона: " + userDTO.getPhoneNumber() +" уже существует!");
        }

        if (userRepository.findByPhoneNumber(userDTO.getEmail()).isPresent()) {
            throw new UserNotCreatedException("Пользователь с таким email: " + userDTO.getEmail() +" уже существует!");
        }

        var newUser = UserMapper.toUser(userDTO);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        userRepository.save(newUser);

        return UserMapper.toDTO(newUser);
    }

    @Transactional()
    @Override
    public UserDTO updateUser(Long id, UserDTO updateUserDTO) {
        var existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с ID: " + id + " не найден!"));

        if (updateUserDTO.getUsername() != null && !updateUserDTO.getUsername().equals(existingUser.getUsername())) {
            userRepository.findByUsername(updateUserDTO.getUsername()).ifPresent(user -> {
                if (!user.getId().equals(id)) {
                    throw new UserNotCreatedException("Пользователь с таким Username: " + updateUserDTO.getUsername() + " уже существует!");
                }
            });
        }

        if (updateUserDTO.getPhoneNumber() != null && !updateUserDTO.getPhoneNumber().equals(existingUser.getPhoneNumber())) {
            userRepository.findByPhoneNumber(updateUserDTO.getPhoneNumber()).ifPresent(user -> {
                if (!user.getId().equals(id)) {
                    throw new UserNotCreatedException("Пользователь с таким номером телефона: " + updateUserDTO.getPhoneNumber() + " уже существует!");
                }
            });
        }

        if (updateUserDTO.getEmail() != null && !updateUserDTO.getEmail().equals(existingUser.getEmail())) {
            userRepository.findByEmail(updateUserDTO.getEmail()).ifPresent(user -> {
                if (!user.getId().equals(id)) {
                    throw new UserNotCreatedException("Пользователь с таким email: " + updateUserDTO.getEmail() + " уже существует!");
                }
            });
        }


        var updateUser = UserMapper.toUser(updateUserDTO);
        updateUser.setId(existingUser.getId());
        updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));

        userRepository.save(updateUser);

        return UserMapper.toDTO(updateUser);
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
