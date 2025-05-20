package iuh.se.team.webbookstore_backend.mapper;

import iuh.se.team.webbookstore_backend.dto.UserDTO;
import iuh.se.team.webbookstore_backend.entities.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setLastName(user.getLastName());
        dto.setFirstName(user.getFirstName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setActivated(user.isActivated());
        dto.setRoles(user.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList()));
        dto.setPassword(user.getPassword());
        return dto;
    }
}