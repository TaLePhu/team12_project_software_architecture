package iuh.se.team.webbookstore_backend.controller;


import iuh.se.team.webbookstore_backend.dao.BookRepository;
import iuh.se.team.webbookstore_backend.dao.ImageRepository;
import iuh.se.team.webbookstore_backend.entities.Book;
import iuh.se.team.webbookstore_backend.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/images")
@CrossOrigin(origins = "http://localhost:3000")
public class ImageController {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bookId") int bookId,
            @RequestParam(value = "isIcon", defaultValue = "false") boolean isIcon) {
        try {
            Optional<Book> bookOptional = bookRepository.findById(bookId);
            if (!bookOptional.isPresent()) {
                return ResponseEntity.badRequest().body("Book not found with ID: " + bookId);
            }

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            Book book = bookOptional.get();

            // Convert image to base64
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Create image entity
            Image image = new Image();
            image.setImageName(file.getOriginalFilename());
            image.setIsIcon(isIcon);
            image.setPath(file.getOriginalFilename());
            image.setImageData("data:" + file.getContentType() + ";base64," + base64Image);
            image.setBook(book);

            // Save to database
            Image savedImage = imageRepository.save(image);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(savedImage);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
        }
    }

}
