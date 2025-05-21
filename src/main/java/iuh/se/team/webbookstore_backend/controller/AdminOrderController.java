package iuh.se.team.webbookstore_backend.controller;



import iuh.se.team.webbookstore_backend.dao.OrderRepository;
import iuh.se.team.webbookstore_backend.dto.OrderRequest;
import iuh.se.team.webbookstore_backend.entities.Order;
import iuh.se.team.webbookstore_backend.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminOrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(order);
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateOrder(@PathVariable int id, @RequestBody OrderRequest orderRequest) {
        Order updated = orderService.updateOrder(id, orderRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<?> confirmDelivery(@PathVariable int orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Order not found");
        }
        Order order = orderOpt.get();

        if (order.isDelivered()) {
            return ResponseEntity.badRequest().body("Order already delivered");
        }

        order.setDelivered(true);
        orderRepository.save(order);

        return ResponseEntity.ok("Order marked as delivered");
    }

}

