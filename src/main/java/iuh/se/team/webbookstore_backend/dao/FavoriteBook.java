package iuh.se.team.webbookstore_backend.dao;

import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface FavoriteBook extends JpaRepository<FavoriteBook, Long> {
}
