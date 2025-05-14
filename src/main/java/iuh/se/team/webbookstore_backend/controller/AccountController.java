package iuh.se.team.webbookstore_backend.controller;

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

import java.util.Map;

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

//    @PostMapping("/sign-in")
//    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest){
//        // Xác thực người dùng bằng tên đăng nhập và mật khẩu
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//            );
//            // Nếu xác thực thành công, tạo token JWT
//            if(authentication.isAuthenticated()){
//                final String jwt = jwtService.generateToken(loginRequest.getUsername());
//                return ResponseEntity.ok(new JwtResponse(jwt));
//            }
//        }catch (AuthenticationException e){
//            // Xác thực không thành công, trả về lỗi hoặc thông báo
//            return ResponseEntity.badRequest().body("Tên đăng nhập hoặc mật khẩu không chính xác.");
//        }
//        return ResponseEntity.badRequest().body("Xác thực không thành công.");
//    }
//    @PostMapping("/sign-in")
//    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
//        User user = userService.findByUsername(req.getUsername());
//        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
//        }
//
//        String token = jwtService.generateToken(user);
//        return ResponseEntity.ok(Map.of("jwt", token));
//    }
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) {
        // Tìm người dùng từ tên đăng nhập
        User user = userService.findByUsername(loginRequest.getUsername());

        // Kiểm tra nếu người dùng không tồn tại hoặc mật khẩu không đúng
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Tên đăng nhập hoặc mật khẩu không chính xác.");
        }

        // Nếu xác thực thành công, tạo token JWT
        try {
            final String jwt = jwtService.generateToken(user); // Sử dụng generateToken với đối tượng User
            return ResponseEntity.ok(new JwtResponse(jwt)); // Trả về token
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Xác thực không thành công.");
        }
    }
}
