package iuh.se.team.webbookstore_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "shipping_method")
public class ShippingMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_method_id")
    private int shippingMethodId;

    @Column(name = "shipping_method_name")
    private String shippingMethodName;

    @Column(name = "description")
    private String description;

    @Column(name = "shipping_cost")
    private double shippingCost;

    @OneToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH , CascadeType.DETACH
    },
            mappedBy = "shippingMethod"
    )
    @JsonIgnore
    private List<Order> orders;
}
