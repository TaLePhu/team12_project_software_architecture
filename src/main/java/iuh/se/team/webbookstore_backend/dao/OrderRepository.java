package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.dto.OrderDTO;
import iuh.se.team.webbookstore_backend.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(path = "orders")
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o")
    List<OrderDTO> findAllProjectedBy();
}
