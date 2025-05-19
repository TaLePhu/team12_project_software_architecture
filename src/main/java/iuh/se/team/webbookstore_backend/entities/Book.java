package iuh.se.team.webbookstore_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "book")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int bookId;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "listed_price")
    private double listedPrice;

    @Column(name = "sale_price")
    private double salePrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "average_rating")
    private double averageRating;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "book",
            cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "book",
            cascade = CascadeType.ALL
    )
    private List<Review> reviews;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "book",
            cascade = {
                    CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH
            }
    )
    private List<OrderDetail> orderDetails;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "book",
            cascade = CascadeType.ALL
    )
    private List<FavoriteBook> favoriteBooks;

    @Column(name = "reserved", columnDefinition = "int default 0")
    private Integer reserved = 0;
}
