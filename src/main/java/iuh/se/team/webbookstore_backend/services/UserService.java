package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dto.UpdateUserRequest;
import iuh.se.team.webbookstore_backend.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    public User findByUsername(String username);
    List<User> findAll();
    User findById(int id);
    User createUser(User user);
    User updateUser(int id, User user);
    boolean deleteUser(int id);

    boolean changePassword(String username, String currentPassword, String newPassword, String confirmPassword);
    User updateUserByUsername(String username, UpdateUserRequest request);

    public boolean assignRolesToUser(int userId, List<Integer> roleIds);
}
