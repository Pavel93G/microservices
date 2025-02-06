package ru.itm.app.api.dto;

import ru.itm.app.storage.models.User;

import java.util.List;

public class RoleDTO {
    private Integer id;
    private String name;
    private List<User> users;

    public RoleDTO() {
    }

    public RoleDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
