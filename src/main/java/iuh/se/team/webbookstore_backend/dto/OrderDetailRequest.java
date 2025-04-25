package iuh.se.team.webbookstore_backend.dto;

import lombok.Data;

@Data
public class OrderDetailRequest {
    private int bookId;
    private int quantity;
    private double salePrice;
}