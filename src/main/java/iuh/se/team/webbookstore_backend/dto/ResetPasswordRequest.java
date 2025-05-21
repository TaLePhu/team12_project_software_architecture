package iuh.se.team.webbookstore_backend.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String otp;
    private String newPassword;
    private String confirmPassword;
}