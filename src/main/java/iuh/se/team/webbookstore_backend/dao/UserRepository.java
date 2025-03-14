package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
