package iuh.se.team.webbookstore_backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDTO {
    int getOrderId();
    String getCreatedDate();
    String getBillingAddress();
    String getShippingAddress();
    double getTotalProductPrice();
    double getShippingFee();
    double getPaymentFee();
    double getTotalPrice();
    String getEmail();
    String getPhoneNumber();
    boolean isConfirmed();
    LocalDateTime getOrderDate();
    LocalDateTime getDeliveryDate();

    UserDTO getUser();
    PaymentMethodDTO getPaymentMethod();
    ShippingMethodDTO getShippingMethod();
    List<OrderDetailDTO> getOrderDetails();

    interface UserDTO {
        int getUserId();
        String getFirstName();
        String getLastName();
    }

    interface PaymentMethodDTO {
        int getPaymentMethodId();
        String getPaymentMethodName();
    }

    interface ShippingMethodDTO {
        int getShippingMethodId();
        String getShippingMethodName();
    }

    interface OrderDetailDTO {
        int getOrderDetailId();
        int getQuantity();
        double getSalePrice();
        BookDTO getBook();

        interface BookDTO {
            int getBookId();
            String getBookName();
        }
    }
}
