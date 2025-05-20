package iuh.se.team.webbookstore_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    private int userId;
    private String lastName;
    private String firstName;
    private String username;
    private String email;
    private String phoneNumber;
    private boolean isActivated;
    private List<String> roles;
    private String password;
}