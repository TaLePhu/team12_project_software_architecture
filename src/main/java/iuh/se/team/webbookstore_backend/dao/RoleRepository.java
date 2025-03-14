package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
