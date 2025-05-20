package iuh.se.team.webbookstore_backend.controller;

import iuh.se.team.webbookstore_backend.dao.BookRepository;
import iuh.se.team.webbookstore_backend.dao.ImageRepository;
import iuh.se.team.webbookstore_backend.dao.ReviewRepository;
import iuh.se.team.webbookstore_backend.dao.UserRepository;
import iuh.se.team.webbookstore_backend.dto.ReviewCreateRequest;
import iuh.se.team.webbookstore_backend.dto.ReviewDTO;
import iuh.se.team.webbookstore_backend.entities.Book;
import iuh.se.team.webbookstore_backend.entities.Review;
import iuh.se.team.webbookstore_backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping()
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewCreateRequest request) {
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());
        Optional<User> userOpt = userRepository.findById(request.getUserId());

        if (bookOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Review review = new Review();
        review.setRatingScore(request.getRatingScore());
        review.setComment(request.getComment());
        review.setBook(bookOpt.get());
        review.setUser(userOpt.get());
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);

        // Bạn có thể chuyển sang DTO nếu cần
        return ResponseEntity.ok().build(); // Hoặc trả DTO mới tạo
    }
}
