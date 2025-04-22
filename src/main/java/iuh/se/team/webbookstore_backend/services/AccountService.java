package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dao.UserRepository;
import iuh.se.team.webbookstore_backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> signUp(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        } else if(userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User newUser = userRepository.save(user);
        return ResponseEntity.ok("signUp successful");
    };


}
