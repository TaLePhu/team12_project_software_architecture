package iuh.se.team.webbookstore_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private int bookId;
    private String bookName;
    private int quantity;
    private double salePrice;
    private int stock;
    private String image;
}
