package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dao.BookRepository;
import iuh.se.team.webbookstore_backend.dao.ImageRepository;
import iuh.se.team.webbookstore_backend.dao.ReviewRepository;
import iuh.se.team.webbookstore_backend.dto.BookSumaryDTO;
import iuh.se.team.webbookstore_backend.dto.ReviewDTO;
import iuh.se.team.webbookstore_backend.entities.Book;
import iuh.se.team.webbookstore_backend.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/{bookId}/images")
    public List<Image> getImagesForBook(@PathVariable int bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (!bookOptional.isPresent()) {
            throw new RuntimeException("Sách không tồn tại với ID: " + bookId);
        }

        Book book = bookOptional.get();
        List<Image> images = imageRepository.findByBook(book);

        return images; // Trả về danh sách hình ảnh trực tiếp thay vì liên kết
    }


//    @GetMapping("/search")
//    public Page<BookSumaryDTO> searchBooks(
//            @RequestParam("keyword") String keyword,
//            @RequestParam("categoryIds") List<Integer> categoryIds,
//            Pageable pageable
//    ) {
//        return bookRepository.searchBooksByKeywordAndCategories(keyword, categoryIds, pageable);
//    }

    @GetMapping("/search/by-keyword")
    public Page<BookSumaryDTO> searchByKeyword(
            @RequestParam("keyword") String keyword,
            Pageable pageable) {
        return bookRepository.findByKeyword(keyword, pageable);
    }

    @GetMapping("/search/by-category-and-keyword")
    public Page<BookSumaryDTO> searchByCategoryAndKeyword(
            @RequestParam("categoryId") int categoryId,
            @RequestParam("keyword") String keyword,
            Pageable pageable) {
        return bookRepository.findByCategoryIdAndKeyword(categoryId, keyword, pageable);
    }

    @GetMapping("/search")
    public Page<BookSumaryDTO> searchAdvanced(
            @RequestParam("keyword") String keyword,
            @RequestParam("categoryIds") List<Integer> categoryIds,
            Pageable pageable) {
        return bookRepository.searchBooksByKeywordAndCategories(keyword, categoryIds, pageable);
    }

    @GetMapping("/{bookId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviewsByBook(@PathVariable int bookId) {
        List<ReviewDTO> reviews = reviewRepository.findByBookBookId(bookId);
        return ResponseEntity.ok(reviews);
    }


}

