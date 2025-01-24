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

    @NotEmpty(message = "Поле фамилия не должно быть пустым!")
    @Size(min = 2, max = 30, message = "Фамилия должно быть от 2 до 30 символов!")
    @Column(name = "lastname")
    private String lastname;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "age")
    private Integer age;

    @NotEmpty(message = "Поле email не должно быть пустым!")
    @Email(message = "Поле email должно быть валидным!")
    @Column(name = "email", unique = true)
    private String email;

    public User(Long id, String firstname, String lastname, LocalDate birthDate, Integer age, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.age = age;
        this.email = email;
    }

    public User() {
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
