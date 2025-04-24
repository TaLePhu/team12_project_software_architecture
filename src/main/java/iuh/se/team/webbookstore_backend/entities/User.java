package iuh.se.team.webbookstore_backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "username")
    private String username;

    @Column(name = "password", length = 512)
    private String password;

    @Column(name = "gender")
    private char gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "is_activated")
    private boolean isActivated;

    @Column(name = "activation_code")
    private String activationCode;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "user")
    private List<FavoriteBook> favoriteBooks;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.DETACH, CascadeType.REFRESH
            },
            mappedBy = "user"
    )
    private List<Order> orders;
}
