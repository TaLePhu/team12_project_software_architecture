package iuh.se.team.webbookstore_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String billingAddress;
    private String shippingAddress;
    private double totalProductPrice;
    private double shippingFee;
    private double paymentFee;
    private double totalPrice;
    private int userId;
    private int paymentMethodId;
    private int shippingMethodId;
    private List<OrderDetailRequest> orderDetails;
    private String email;
    private String phoneNumber;
    private String lastName;
    private String firstName;
}