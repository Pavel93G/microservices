package ru.itm.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itm.app.models.User;
import ru.itm.app.security.UserDetailsImp;
import ru.itm.app.service.UserService;

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
        var name = authentication.getName();

        var userDetails = (UserDetailsImp) userService.loadUserByUsername(name);
        var user = userDetails.getUser();
        model.addAttribute("user", user);
        return "user/showUserInfo";
    }
}
