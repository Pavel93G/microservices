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
    public ResponseEntity<?> findAllUsers() {
        System.out.println("=== Получаем список пользователей перед операциями ===");
        var allUsersBefore = userService.findAllUsers();

        System.out.println("=== Добавляем пользователя ===");
        String addResponse = userService.addUser();
        System.out.println("Ответ на добавление: " + addResponse);

        System.out.println("=== Обновляем пользователя ===");
        String updateResponse = userService.updateUser();
        System.out.println("Ответ на обновление: " + updateResponse);

        System.out.println("=== Удаляем пользователя ===");
        String deleteResponse = userService.deletedUser();
        System.out.println("Ответ на удаление: " + deleteResponse);

        System.out.println("=== Получаем список пользователей после всех операций ===");
        var allUsersAfter = userService.findAllUsers();

        return new ResponseEntity<>(allUsersAfter, HttpStatus.OK);
    }
}
