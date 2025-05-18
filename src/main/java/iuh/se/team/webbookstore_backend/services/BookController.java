package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dao.BookRepository;
import iuh.se.team.webbookstore_backend.dao.ImageRepository;
import iuh.se.team.webbookstore_backend.entities.Book;
import iuh.se.team.webbookstore_backend.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/search/findByBookNameContainingAndCategoryIds")
    public Page<Book> findByBookNameAndCategories(
            @RequestParam("bookName") String bookName,
            @RequestParam("categoryId") List<Integer> categoryIds,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "bookId,desc") String[] sort
    ) {
        String[] sortParams = sort[0].split(",");
        String sortField = sortParams[0];
        String sortDirection = sortParams.length > 1 ? sortParams[1] : "asc";

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                        sortField
                )
        );

        return bookRepository.findDistinctByBookNameContainingAndCategories_CategoryIdIn(bookName, categoryIds, pageable);
    }

}
