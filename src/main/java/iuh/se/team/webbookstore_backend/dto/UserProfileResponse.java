package iuh.se.team.webbookstore_backend.dto;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;
    private char gender;
    private String username;
}