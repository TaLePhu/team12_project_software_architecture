package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserSevice extends UserDetailsService {
    public User findByUsername(String username);
}
