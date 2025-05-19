package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.dto.BookSumaryDTO;
import iuh.se.team.webbookstore_backend.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RepositoryRestResource(path = "books")
public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByBookId(int bookId);
    Page<Book> findByBookNameContaining(@RequestParam("bookName") String bookName, Pageable pageable);

    Page<Book> findByCategories_CategoryId(@RequestParam("categoryId") int categoryId, Pageable pageable);

    Page<Book> findByBookNameContainingAndCategories_CategoryId(@RequestParam("bookName") String bookName, @RequestParam("categoryId") int categoryId, Pageable pageable);

//    Page<Book> findDistinctByBookNameContainingAndCategories_CategoryIdIn(
//            @Param("bookName") String bookName,
//            @Param("categoryIds") List<Integer> categoryIds,
//            Pageable pageable
//    );

    // Mới: Tìm sách theo tên và lọc nhiều categoryId (nhiều thể loại)
    // Trường hợp có keyword và có categoryIds
    @Query("SELECT DISTINCT b FROM Book b JOIN b.categories c " +
            "WHERE (:bookName IS NULL OR LOWER(b.bookName) LIKE LOWER(CONCAT('%', :bookName, '%'))) " +
            "AND c.categoryId IN :categoryIds")
    Page<Book> searchBooksByNameAndCategories(
            @Param("bookName") String bookName,
            @Param("categoryIds") List<Integer> categoryIds,
            Pageable pageable);

    // Trường hợp chỉ có keyword (không lọc category)
    @Query("SELECT DISTINCT b FROM Book b " +
            "WHERE (:bookName IS NULL OR LOWER(b.bookName) LIKE LOWER(CONCAT('%', :bookName, '%')))")
    Page<Book> searchBooksByNameOnly(
            @Param("bookName") String bookName,
            Pageable pageable);

    // Tìm theo tên sách (không phân biệt hoa thường)
    Page<Book> findByBookNameContainingIgnoreCase(String bookName, Pageable pageable);

    // Tìm theo nhiều category
    Page<Book> findByCategories_CategoryIdIn(List<Integer> categoryIds, Pageable pageable);

    Page<Book> findByBookNameContainingIgnoreCaseAndCategories_CategoryIdIn(String keyword, List<Integer> categoryIds, Pageable pageable);

    @Query("""
        SELECT b FROM Book b 
        JOIN b.categories c 
        WHERE LOWER(b.bookName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        AND c.categoryId IN :categoryIds
        GROUP BY b.bookId
    """)
    Page<BookSumaryDTO> searchBooksByKeywordAndCategories(
            @Param("keyword") String keyword,
            @Param("categoryIds") List<Integer> categoryIds,
            Pageable pageable
    );
}
