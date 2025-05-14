package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@RepositoryRestResource(path = "books")
public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByBookId(int bookId);
    Page<Book> findByBookNameContaining(@RequestParam("bookName") String bookName, Pageable pageable);

    Page<Book> findByCategories_CategoryId(@RequestParam("categoryId") int categoryId, Pageable pageable);

    Page<Book> findByBookNameContainingAndCategories_CategoryId(@RequestParam("bookName") String bookName, @RequestParam("categoryId") int categoryId, Pageable pageable);
}
