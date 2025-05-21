package iuh.se.team.webbookstore_backend.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;
    private Character gender;
    // Không cho phép cập nhật username, password, roles qua API này!
}