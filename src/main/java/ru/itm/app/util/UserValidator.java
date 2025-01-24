package ru.itm.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
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
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (user.getBirthDate() != null && user.getBirthDate().isAfter(LocalDate.now())) {
            errors.rejectValue("birthDate", "", "Дата рождения не может быть в будущем!");
        }

        if (user.getFirstname() != null && !user.getFirstname().matches("[A-Za-zА-Яа-я]+")) {
            errors.rejectValue("firstname", "", "Имя должно содержать только буквы!");
        }

        if (user.getLastname() != null && !user.getLastname().matches("[A-Za-zА-Яа-я]+")) {
            errors.rejectValue("lastname", "", "Фамилия должна содержать только буквы!");
        }

        if (userService.findUserByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "Данный email уже зарегистрирован!");
        }

        if (userService.findUserByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            errors.rejectValue("phoneNumber", "", "Данный номер телефона уже зарегистрирован!");
        }
    }
}
