package ru.itm.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itm.app.dao.UserDAO;
import ru.itm.app.models.User;

import java.time.LocalDate;

@Component
public class UserValidator implements Validator {
    private final UserDAO userDAO;

    @Autowired
    public UserValidator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (user.getBirthDate() != null && user.getBirthDate().isAfter(LocalDate.now())) {
            errors.rejectValue("birthDate", "", "Дата рождения не может быть в будущем!");
        }

        if (user.getBirthDate() != null && user.getAge() != null) {
            int calculatedAge = LocalDate.now().getYear() - user.getBirthDate().getYear();
            if (user.getAge() != calculatedAge) {
                errors.rejectValue("age", "", "Возраст не соответствует дате рождения!");
            }
        }

        if (user.getFirstname() != null && !user.getFirstname().matches("[A-Za-zА-Яа-я]+")) {
            errors.rejectValue("firstname", "", "Имя должно содержать только буквы!");
        }

        if (user.getLastname() != null && !user.getLastname().matches("[A-Za-zА-Яа-я]+")) {
            errors.rejectValue("lastname", "", "Фамилия должна содержать только буквы!");
        }
    }
}
