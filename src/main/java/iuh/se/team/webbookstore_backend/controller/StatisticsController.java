package iuh.se.team.webbookstore_backend.controller;

import iuh.se.team.webbookstore_backend.dao.BookRepository;
import iuh.se.team.webbookstore_backend.dao.OrderRepository;
import iuh.se.team.webbookstore_backend.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "http://localhost:3000")
public class StatisticsController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();

            // Tổng số người dùng
            long totalUsers = userRepository.count();
            statistics.put("totalUsers", totalUsers);

            // Tổng số sản phẩm
            long totalProducts = bookRepository.count();
            statistics.put("totalProducts", totalProducts);

            // Tổng doanh thu
            double totalRevenue = orderRepository.findAll().stream()
                    .filter(order -> order.isConfirmed()) // Chỉ tính các đơn đã xác nhận
                    .mapToDouble(order -> order.getTotalPrice())
                    .sum();
            statistics.put("totalRevenue", totalRevenue);

            // Số đơn hàng đã xác nhận
            long totalOrders = orderRepository.findAll().stream()
                    .filter(order -> order.isConfirmed())
                    .count();
            statistics.put("totalOrders", totalOrders);

            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching dashboard statistics: " + e.getMessage());
        }
    }
}
