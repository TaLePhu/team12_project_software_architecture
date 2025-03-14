package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
