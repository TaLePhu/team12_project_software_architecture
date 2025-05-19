package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dao.BookRepository;
import iuh.se.team.webbookstore_backend.dao.ImageRepository;
import iuh.se.team.webbookstore_backend.dto.BookSumaryDTO;
import iuh.se.team.webbookstore_backend.entities.Book;
import iuh.se.team.webbookstore_backend.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ImageRepository imageRepository;

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
//    public ResponseEntity<?> searchBooks(
//            @RequestParam(name = "keyword", required = false) String keyword,
//            @RequestParam(name = "categoryIds", required = false) String categoryIdsCsv,
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "5") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        List<Integer> categoryIds = new ArrayList<>();
//
//        if (categoryIdsCsv != null && !categoryIdsCsv.isEmpty()) {
//            categoryIds = Arrays.stream(categoryIdsCsv.split(","))
//                    .map(String::trim)
//                    .map(Integer::parseInt)
//                    .collect(Collectors.toList());
//        }
//
//        Page<Book> result;
//
//        if (!categoryIds.isEmpty()) {
//            result = bookRepository.searchBooksByNameAndCategories(keyword, categoryIds, pageable);
//        } else {
//            result = bookRepository.searchBooksByNameOnly(keyword, pageable);
//        }
//
//        return ResponseEntity.ok(result);
//    }

    @GetMapping("/search")
    public Page<BookSumaryDTO> searchBooks(
            @RequestParam("keyword") String keyword,
            @RequestParam("categoryIds") List<Integer> categoryIds,
            Pageable pageable
    ) {
        return bookRepository.searchBooksByKeywordAndCategories(keyword, categoryIds, pageable);
    }


}

