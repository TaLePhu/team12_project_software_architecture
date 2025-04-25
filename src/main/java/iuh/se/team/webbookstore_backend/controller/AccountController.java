package iuh.se.team.webbookstore_backend.controller;

import iuh.se.team.webbookstore_backend.entities.User;
import iuh.se.team.webbookstore_backend.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;



    @PostMapping("/sign-up")
    public ResponseEntity<?> signUpUser(@Validated @RequestBody User user) {
      ResponseEntity<?> response = accountService.signUp(user);
      return response;
    };

    @GetMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam String email, @RequestParam String activationCode) {
        ResponseEntity<?> response = accountService.activateAccount(email, activationCode);
        return response;
    };
}
