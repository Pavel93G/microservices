package ru.itm.app.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itm.app.api.dto.UserDTO;
import ru.itm.app.storage.service.RoleService;
import ru.itm.app.storage.service.UserService;
import ru.itm.app.api.util.UserValidator;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminRESTController {
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public AdminRESTController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @GetMapping()
    public List<UserDTO> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserDTO showUserById(@PathVariable("id") Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<?> creatNewUser(@RequestBody @Valid UserDTO newUserDTO,
                                          BindingResult bindingResult,
                                          UriComponentsBuilder componentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        } else {
            var createdUser = userService.addUser(newUserDTO);

            return ResponseEntity.created(componentsBuilder
                            .pathSegment("{id}")
                            .build(Map.of("id", createdUser.getId())))
                    .body(newUserDTO);
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid UserDTO updUserDTO,
                                    BindingResult bindingResult) {
        userValidator.validate(updUserDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        userService.updateUser(id, updUserDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
