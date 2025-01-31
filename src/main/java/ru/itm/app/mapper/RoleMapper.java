package ru.itm.app.mapper;

import ru.itm.app.dto.RoleDTO;
import ru.itm.app.models.Role;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RoleMapper {

    public static RoleDTO toDTO(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getName()
        );
    }

    public static List<RoleDTO> toDTOList (List<Role> roles) {
        return roles.stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static Role toRole(RoleDTO roleDTO) {
        var role = new Role(
                roleDTO.getName()
        );

        if (roleDTO.getId() != null) {
            role.setId(roleDTO.getId());
        }

        return role;
    }

    public static List<Role> toRoleList (List<RoleDTO> roleDTOList) {
        return roleDTOList.stream()
                .map(RoleMapper::toRole)
                .collect(Collectors.toList());
    }
}
