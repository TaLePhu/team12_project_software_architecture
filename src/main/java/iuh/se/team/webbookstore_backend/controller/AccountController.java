package iuh.se.team.webbookstore_backend.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import iuh.se.team.webbookstore_backend.dto.ChangePasswordRequest;
import iuh.se.team.webbookstore_backend.dto.UpdateUserRequest;
import iuh.se.team.webbookstore_backend.dto.UserProfileResponse;
import iuh.se.team.webbookstore_backend.entities.User;
import iuh.se.team.webbookstore_backend.security.JwtResponse;
import iuh.se.team.webbookstore_backend.security.LoginRequest;
import iuh.se.team.webbookstore_backend.services.AccountService;
import iuh.se.team.webbookstore_backend.services.JwtService;
import iuh.se.team.webbookstore_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, k -> Bucket.builder()
                .addLimit(Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)))) // 5 request/phút
                .build());
    }


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

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) {
        String key = loginRequest.getUsername(); // Hoặc IP người dùng nếu muốn giới hạn theo IP

        Bucket bucket = resolveBucket(key);
        // Tìm người dùng từ tên đăng nhập
       // User user = userService.findByUsername(loginRequest.getUsername());

        if (bucket.tryConsume(1)) {
            // Xử lý đăng nhập như bình thường
            User user = userService.findByUsername(loginRequest.getUsername());
            if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("Tên đăng nhập hoặc mật khẩu không chính xác.");
            }
            try {
                final String jwt = jwtService.generateToken(user);
                // Kiểm tra xem user có role là ADMIN không
                String role = user.getRoles().stream()
                        .anyMatch(r -> r.getRoleName().equalsIgnoreCase("ADMIN")) ? "ADMIN" : "USER";

                return ResponseEntity.ok(new JwtResponse(jwt, role));

            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Xác thực không thành công.");
            }
        } else {
            // Vượt quá giới hạn
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Bạn đã gửi quá nhiều yêu cầu. Vui lòng thử lại sau.");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        String username = authentication.getName();
        boolean result = userService.changePassword(
                username,
                request.getCurrentPassword(),
                request.getNewPassword(),
                request.getConfirmPassword()
        );
        if (result) {
            return ResponseEntity.ok("Đổi mật khẩu thành công");
        } else {
            return ResponseEntity.badRequest().body("Đổi mật khẩu thất bại. Kiểm tra lại thông tin.");
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserRequest request, Authentication authentication) {
        String username = authentication.getName();
        User updated = userService.updateUserByUsername(username, request);
        if (updated != null) {
            // Chuyển sang DTO để trả về dữ liệu gọn
            UserProfileResponse response = new UserProfileResponse();
            response.setFirstName(updated.getFirstName());
            response.setLastName(updated.getLastName());
            response.setEmail(updated.getEmail());
            response.setPhoneNumber(updated.getPhoneNumber());
            response.setBillingAddress(updated.getBillingAddress());
            response.setShippingAddress(updated.getShippingAddress());
            response.setGender(updated.getGender());
            response.setUsername(updated.getUsername());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Cập nhật thông tin thất bại.");
        }
    }
}
