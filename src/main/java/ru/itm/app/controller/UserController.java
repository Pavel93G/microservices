package ru.itm.app.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itm.app.models.User;
import ru.itm.app.service.UserService;
import ru.itm.app.util.UserValidator;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public String findAllUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users/index";
    }

    @GetMapping("/{id}")
    public String showUserById(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "users/show";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "users/new";
    }

    @PostMapping()
    public String creatNewUser(@ModelAttribute("user") @Valid User newUser,
                               BindingResult bindingResult) {
        userValidator.validate(newUser, bindingResult);

        if (bindingResult.hasErrors()) {
            return "users/new";
        }
        userService.addUser(newUser);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User updUser,
                         BindingResult bindingResult, @PathVariable("id") Long id) {
        userValidator.validate(updUser, bindingResult);

        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        userService.updateUser(id, updUser);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
