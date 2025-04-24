package iuh.se.team.webbookstore_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int imageId;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "is_icon")
    private Boolean isIcon;

    @Column(name = "path")
    private String path;

    @Column(name = "image_data", columnDefinition = "LONGTEXT")
    @Lob
    private String imageData;

    @ManyToOne(cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    })
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnore
    private Book book;
}
