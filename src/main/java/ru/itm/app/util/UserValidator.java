package ru.itm.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itm.app.dto.UserDTO;
import ru.itm.app.models.User;
import ru.itm.app.service.UserService;

import java.time.LocalDate;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        if (userDTO.getBirthDate() != null) {
            LocalDate today = LocalDate.now();
            LocalDate minDate = LocalDate.of(1900, 1, 1);

            if (userDTO.getBirthDate().isAfter(today)) {
                errors.rejectValue("birthDate", "Дата рождения не может быть в будущем");
            }
            if (userDTO.getBirthDate().isBefore(minDate)) {
                errors.rejectValue("birthDate", "Дата рождения не может быть раньше 1900 года");
            }
        }

        if (userDTO.getFirstname() != null && !userDTO.getFirstname().matches("[A-Za-zА-Яа-я]+")) {
            errors.rejectValue("firstname", "", "Имя должно содержать только буквы!");
        }

        if (userDTO.getLastname() != null && !userDTO.getLastname().matches("[A-Za-zА-Яа-я]+")) {
            errors.rejectValue("lastname", "", "Фамилия должна содержать только буквы!");
        }

        if (userDTO.getEmail() != null) {
            userService.findUserByEmail(userDTO.getEmail()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(userDTO.getId())) {
                    errors.rejectValue("email", "", "Данный email уже зарегистрирован!");
                }
            });
        }

        if (userDTO.getPhoneNumber() != null) {
            userService.findUserByPhoneNumber(userDTO.getPhoneNumber()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(userDTO.getId())) {
                    errors.rejectValue("phoneNumber", "", "Данный номер телефона уже зарегистрирован!");
                }
            });
        }

        if (userDTO.getUsername() != null) {
            userService.findUserByUsername(userDTO.getUsername()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(userDTO.getId())) {
                    errors.rejectValue("username", "", "Имя пользователя должно быть уникальным!");
                }
            });
        }
    }
}
