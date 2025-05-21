package iuh.se.team.webbookstore_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "total_product_price")
    private double totalProductPrice;

    @Column(name = "shipping_fee")
    private double shippingFee;

    @Column(name = "payment_fee")
    private double paymentFee;

    @Column(name = "total_price")
    private double totalPrice;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("orders")
    private User user;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH})
    @JoinColumn(name = "payment_method_id")
    @JsonIgnoreProperties("orders")
    private PaymentMethod paymentMethod;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH})
    @JoinColumn(name = "shipping_method_id")
    @JsonIgnoreProperties("orders")
    private ShippingMethod shippingMethod;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "order"
    )
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "confirmed")
    private boolean confirmed = false;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "delivered")
    private boolean delivered = false;
}
