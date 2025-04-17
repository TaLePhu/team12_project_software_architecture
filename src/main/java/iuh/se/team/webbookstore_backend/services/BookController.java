package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dao.BookRepository;
import iuh.se.team.webbookstore_backend.dao.ImageRepository;
import iuh.se.team.webbookstore_backend.entities.Book;
import iuh.se.team.webbookstore_backend.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Lấy danh sách ảnh của một cuốn sách
//    @GetMapping("/{bookId}/list-images")
//    public List<Image> getImagesForBook(@PathVariable int bookId) {
//        // Kiểm tra xem cuốn sách có tồn tại trong cơ sở dữ liệu không
//        Optional<Book> bookOptional = bookRepository.findById(bookId);
//
//        if (!bookOptional.isPresent()) {
//            throw new RuntimeException("Sách không tồn tại với ID: " + bookId);
//        }
//
//        // Lấy tất cả ảnh liên kết với cuốn sách
//        Book book = bookOptional.get();
//        return imageRepository.findByBook(book);
//    }
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
}
