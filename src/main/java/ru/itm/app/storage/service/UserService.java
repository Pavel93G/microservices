package ru.itm.app.storage.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.itm.app.api.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    List<UserDTO> findAllUsers();
    UserDTO findUserById(Long id);
    Optional<UserDTO> findUserByUsername(String username);
    Optional<UserDTO> findUserByEmail(String email);
    Optional<UserDTO> findUserByPhoneNumber(String phoneNumber);
    UserDTO addUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO updateUserDTO);
    void deleteUser(Long id);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
