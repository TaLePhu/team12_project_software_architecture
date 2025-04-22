package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Integer> {

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);
}
