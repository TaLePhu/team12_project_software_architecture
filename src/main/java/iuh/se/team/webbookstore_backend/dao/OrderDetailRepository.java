package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
