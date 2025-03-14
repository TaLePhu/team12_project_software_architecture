package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.FavoriteBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "favorite-books")
public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {
}
