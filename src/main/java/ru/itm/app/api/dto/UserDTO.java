package ru.itm.app.api.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itm.app.storage.models.Role;

import java.time.LocalDate;
import java.util.Set;

public class UserDTO {
    private Long id;

    @NotEmpty(message = "Имя пользователя не должно быть пустым")
    @Size(min = 5, max = 20, message = "Имя пользователя должно быть не меньше 5 знаков и не больше 20!")
    private String username;

    @NotBlank(message = "Поле пароль не должно быть пустым!")
    @Size(min = 8, max = 255, message = "Пароль должен быть не меньше 8 знаков и не больше 30!")
    private String password;

    @NotEmpty(message = "Поле имя не должно быть пустым!")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов!")
    private String firstname;

    @NotEmpty(message = "Поле фамилия не должно быть пустым!")
    @Size(min = 2, max = 30, message = "Фамилия должно быть от 2 до 30 символов!")
    private String lastname;

    @Past(message = "Дата рождения должна быть в прошлом")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Дата рождения обязательна")
    private LocalDate birthDate;

    @NotBlank(message = "Поле email не должно быть пустым и содержать пробелы!")
    @Email(message = "Полу email должно быть валидным!")
    private String email;

    @NotEmpty(message = "Поле номер телефона не должно быть пустым!")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный номер телефона.")
    private String phoneNumber;

    private Set<Role> roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String password, String firstname, String lastname, LocalDate birthDate, String email,
                String phoneNumber, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
