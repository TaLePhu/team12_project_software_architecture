package iuh.se.team.webbookstore_backend.dto;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}