package ru.itm.app.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itm.app.api.dto.UserDTO;
import ru.itm.app.storage.service.RoleService;
import ru.itm.app.storage.service.UserService;
import ru.itm.app.api.util.UserValidator;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }


    @GetMapping()
    public String findAllUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users/index";
    }

    @GetMapping("/users/{id}")
    public String showUserById(@PathVariable("id") Long id, Model model) {
        var user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "admin/users/show";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", roleService.findAllRoles());
        return "admin/users/new";
    }

    @PostMapping("/users")
    public String creatNewUser(@ModelAttribute("user") @Valid UserDTO newUserDTO,
                               BindingResult bindingResult,
                               Model model) {
        userValidator.validate(newUserDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAllRoles());
            return "admin/users/new";
        }

        userService.addUser(newUserDTO);
        return "redirect:/admin";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(Model model, @PathVariable("id") Long id) {
        var user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAllRoles());
        return "admin/users/edit";
    }

    @PatchMapping("/users/{id}")
    public String update(@ModelAttribute("user") @Valid UserDTO updUserDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id,
                         Model model) {
        userValidator.validate(updUserDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAllRoles());
            return "admin/users/edit";
        }

        userService.updateUser(id, updUserDTO);
        return "redirect:/admin";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        System.out.println("Юзер удален");
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
