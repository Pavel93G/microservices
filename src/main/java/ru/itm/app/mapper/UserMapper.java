package ru.itm.app.mapper;

import ru.itm.app.dto.UserDTO;
import ru.itm.app.models.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstname(),
                user.getLastname(),
                user.getBirthDate(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRoles()
        );
    }

    public static List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static User toUser(UserDTO userDTO) {
        var user = new User(
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getFirstname(),
                userDTO.getLastname(),
                userDTO.getBirthDate(),
                userDTO.getEmail(),
                userDTO.getPhoneNumber(),
                userDTO.getRoles()
        );

        if (userDTO.getId() != null) {
            user.setId(userDTO.getId());
        }

        return user;
    }

    public static List<User> toUsersList(List<UserDTO> userDTOList) {
        return userDTOList.stream()
                .map(UserMapper::toUser)
                .collect(Collectors.toList());
    }
}
