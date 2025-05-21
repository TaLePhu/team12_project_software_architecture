package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dao.PasswordResetTokenRepository;
import iuh.se.team.webbookstore_backend.dao.UserRepository;
import iuh.se.team.webbookstore_backend.dto.ForgotPasswordRequest;
import iuh.se.team.webbookstore_backend.dto.ResetPasswordRequest;
import iuh.se.team.webbookstore_backend.entities.Notify;
import iuh.se.team.webbookstore_backend.entities.PasswordResetToken;
import iuh.se.team.webbookstore_backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    // Gửi OTP về email
    public ResponseEntity<?> sendOtpToEmail(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("Email không tồn tại.");
        }

        // Xóa OTP cũ nếu có
        passwordResetTokenRepository.deleteByUser(user);

        // Tạo OTP mới
        String otp = String.format("%06d", new Random().nextInt(999999));
        PasswordResetToken token = new PasswordResetToken();
        token.setOtp(otp);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        passwordResetTokenRepository.save(token);

        // Gửi email
        String subject = "Mã OTP đặt lại mật khẩu WebBookStore";
        String text = "<html><body>Mã OTP đặt lại mật khẩu của bạn là: <b>" + otp + "</b><br/>"
                + "Mã này có hiệu lực trong 10 phút.</body></html>";
        emailService.sendEmail("lephu18062@gmail.com", user.getEmail(), subject, text, true);

        return ResponseEntity.ok("Đã gửi OTP về email.");
    }

    // Xác thực OTP và đổi mật khẩu
    public ResponseEntity<?> resetPasswordWithOtp(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("Email không tồn tại.");
        }

        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepository.findByUserAndOtp(user, request.getOtp());
        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("OTP không hợp lệ.");
        }

        PasswordResetToken token = tokenOpt.get();
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(token);
            return ResponseEntity.badRequest().body("OTP đã hết hạn.");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu xác nhận không khớp.");
        }

        // Đổi mật khẩu
        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Xóa OTP sau khi dùng
        passwordResetTokenRepository.delete(token);

        return ResponseEntity.ok("Đặt lại mật khẩu thành công.");
    }

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
