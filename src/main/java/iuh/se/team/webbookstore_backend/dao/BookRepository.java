package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
