package ru.itm.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itm.app.api.dto.UserDTO;
import ru.itm.app.api.mapper.UserMapper;
import ru.itm.app.api.security.UserDetailsImp;
import ru.itm.app.storage.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String showUserInfo(Model model) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();

        var userDetails = (UserDetailsImp) userService.loadUserByUsername(username);
        var user = userDetails.getUser();
        var userDTO = UserMapper.toDTO(user);
        model.addAttribute("user", userDTO);

        return "user/showUserInfo";
    }
}
