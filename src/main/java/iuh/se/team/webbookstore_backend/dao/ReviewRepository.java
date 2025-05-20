package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.dto.ReviewDTO;
import iuh.se.team.webbookstore_backend.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(path = "reviews")
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<ReviewDTO> findByBookBookId(int bookId);
}
