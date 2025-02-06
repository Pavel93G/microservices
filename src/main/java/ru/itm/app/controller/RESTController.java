package ru.itm.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itm.app.service.UserService;

@RestController
@RequestMapping("api/v1/app")
public class RESTController {
    private final UserService userService;

    @Autowired
    public RESTController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/execute")
    public ResponseEntity<?> execute() {

        var allUsers = userService.findAllUsers();
//        userService.addUser();

        return new ResponseEntity<>(allUsers,HttpStatus.OK);
    }
}
