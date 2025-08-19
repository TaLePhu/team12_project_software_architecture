package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dto.OrderDTO;
import iuh.se.team.webbookstore_backend.dto.OrderRequest;
import iuh.se.team.webbookstore_backend.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);
    List<OrderDTO> getAllOrders();
    Optional<Order> getOrderById(int id);
    Order updateOrder(int id, OrderRequest orderRequest);
    void deleteOrder(int id);
}
