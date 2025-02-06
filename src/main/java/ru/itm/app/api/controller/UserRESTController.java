package ru.itm.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itm.app.api.exceptions.BadRequestException;
import ru.itm.app.api.mapper.UserMapper;
import ru.itm.app.api.security.UserDetailsImp;
import ru.itm.app.storage.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserRESTController {
    private final UserService userService;

    @Autowired
    public UserRESTController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> showUserInfo() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("Пользователь не аутентифицирован!");
        }
        var username = authentication.getName();

        var userDetails = (UserDetailsImp) userService.loadUserByUsername(username);
        var user = userDetails.getUser();
        var userDTO = UserMapper.toDTO(user);

        return ResponseEntity.ok(userDTO);
    }
}
