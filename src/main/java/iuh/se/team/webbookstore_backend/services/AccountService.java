package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dao.UserRepository;
import iuh.se.team.webbookstore_backend.entities.Notify;
import iuh.se.team.webbookstore_backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

//    public ResponseEntity<?> signUp(User user) {
//        if(userRepository.existsByUsername(user.getUsername())) {
//            return ResponseEntity.badRequest().body("Username already exists");
//        } else if(userRepository.existsByEmail(user.getEmail())) {
//            return ResponseEntity.badRequest().body("Email already exists");
//        }
//
//        //encode user password
//        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
//
//        //create and assign Activation Code
//        user.setActivationCode(createActivationCode());
//        user.setActivated(false);
//
//        //save user to database
//        User newUser = userRepository.save(user);
//
//        //gửi email kích hoạt cho user để kích hoạt
//        sendActivationEmail(newUser.getEmail(), newUser.getActivationCode());
//
//        return ResponseEntity.ok("signUp successful");
//    };
public ResponseEntity<?> signUp(User user) {
    if (userRepository.existsByUsername(user.getUsername())) {
        return ResponseEntity.badRequest().body("Username already exists");
    } else if (userRepository.existsByEmail(user.getEmail())) {
        return ResponseEntity.badRequest().body("Email already exists");
    }

    // Gán userId < 9000 nếu có thể
    Integer maxUserIdUnder9000 = userRepository.findMaxUserIdUnder9000();
    int nextUserId = (maxUserIdUnder9000 == null || maxUserIdUnder9000 < 1) ? 1 : maxUserIdUnder9000 + 1;

//    user.setUserId(nextUserId); // Gán id thủ công
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    user.setActivationCode(createActivationCode());
    user.setActivated(false);

    User newUser = userRepository.save(user);
    sendActivationEmail(newUser.getEmail(), newUser.getActivationCode());

    return ResponseEntity.ok("signUp successful");
}

    // create activation code
    private String createActivationCode() {
        return UUID.randomUUID().toString();
    }

    private void sendActivationEmail(String email, String activationCode) {
        String subject = "Kích hoạt tài khoản của bạn tại WebBookStore";
        String text = "<html><body>"
                + "Vui lòng sử dụng mã sau để kích hoạt cho tài khoản <b>" + email + "</b>:"
                + "<br/><h1>" + activationCode + "</h1>"
                + "<br/> Click vào đường link để kích hoạt tài khoản: "
                + "<br/> <a href='http://localhost:3000/activate/" + email + "/" + activationCode + "'>"
                + "http://localhost:3000/activate/" + email + "/" + activationCode + "</a>"
                + "</body></html>";

        emailService.sendEmail("lephu18062@gmail.com", email, subject, text, true);

    }

    public ResponseEntity<?> activateAccount(String email, String activationCode) {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            return ResponseEntity.badRequest().body(new Notify("User not found!"));
        }

        if(user.isActivated()) {
           return ResponseEntity.badRequest().body(new Notify("User already activated!"));
        }

        if(activationCode.equals(user.getActivationCode())) {
            user.setActivated(true);
            userRepository.save(user);
            return ResponseEntity.ok(new Notify("Account activated successfully!"));
        } else {
            return ResponseEntity.badRequest().body(new Notify("Invalid activation code!"));
        }

    }

}
