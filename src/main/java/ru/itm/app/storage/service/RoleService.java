package ru.itm.app.storage.service;

import ru.itm.app.storage.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAllRoles();
}
