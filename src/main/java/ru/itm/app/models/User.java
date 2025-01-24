package ru.itm.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Поле имя не должно быть пустым!")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов!")
    @Column(name = "firstname")
    private String firstname;

    @NotEmpty(message = "Полу фамилия не должно быть пустым!")
    @Size(min = 2, max = 30, message = "Фамилия должно быть от 2 до 30 символов!")
    @Column(name = "lastname")
    private String lastname;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotBlank(message = "Полу email не должно быть пустым и содержать пробелы!")
    @Email(message = "Полу email должно быть валидным!")
    @Column(name = "email", unique = true)
    private String email;

    @NotEmpty(message = "Поле номер телефона не должно быть пустым!")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный номер телефона.")
    @Column(name = "phone_number")
    private String phoneNumber;

    public User() {
    }

    public User(String firstname, String lastname, LocalDate birthDate, String email, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname (String firstname) {
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

    public void setPhoneNumber (String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
