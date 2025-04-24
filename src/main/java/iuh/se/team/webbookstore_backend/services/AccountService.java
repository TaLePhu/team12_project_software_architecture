package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dao.UserRepository;
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

    public ResponseEntity<?> signUp(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        } else if(userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        //encode user password
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        //create and assign Activation Code
        user.setActivationCode(createActivationCode());
        user.setActivated(false);

        //save user to database
        User newUser = userRepository.save(user);

        //gửi email kích hoạt cho user để kích hoạt
        sendActivationEmail(newUser.getEmail(), newUser.getActivationCode());

        return ResponseEntity.ok("signUp successful");
    };

    // create activation code
    private String createActivationCode() {
        return UUID.randomUUID().toString();
    }

    private void sendActivationEmail(String email, String activationCode) {
        String subject = "Kích hoạt tài khoản của bạn tại WebBanSach";
        String text = "<html><body>"
                + "Vui lòng sử dụng mã sau để kích hoạt cho tài khoản <b>" + email + "</b>:"
                + "<br/><h1>" + activationCode + "</h1>"
                + "<br/> Click vào đường link để kích hoạt tài khoản: "
                + "<br/> <a href='http://localhost:3000/kich-hoat/" + email + "/" + activationCode + "'>"
                + "http://localhost:3000/kich-hoat/" + email + "/" + activationCode + "</a>"
                + "</body></html>";

        emailService.sendEmail("lephu18062@gmail.com", email, subject, text, true);

    }

}
