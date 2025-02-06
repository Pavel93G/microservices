package ru.itm.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itm.app.models.Role;
import ru.itm.app.repositories.RoleRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class RoleServiceImp implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
