package iuh.se.team.webbookstore_backend.scheduled;

import iuh.se.team.webbookstore_backend.dao.BookRepository;
import iuh.se.team.webbookstore_backend.dao.OrderDetailRepository;
import iuh.se.team.webbookstore_backend.dao.OrderRepository;
import iuh.se.team.webbookstore_backend.entities.Book;
import iuh.se.team.webbookstore_backend.entities.Order;
import iuh.se.team.webbookstore_backend.entities.OrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class OrderClearupTask {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BookRepository bookRepository;

    // Chạy mỗi 1 giờ (3600000 ms)
    @Scheduled(fixedRate = 3600000)
    public void deleteExpiredOrders() {
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);

        // Tìm các đơn hàng chưa xác nhận và có orderDate trước expiredTime
        List<Order> expiredOrders = orderRepository.findByConfirmedFalseAndOrderDateBefore(expiredTime);

        for (Order order : expiredOrders) {
            // Trả lại tồn kho các sách đã giữ trong đơn hàng
            List<OrderDetail> details = orderDetailRepository.findByOrder(order);
            for (OrderDetail detail : details) {
                Book book = detail.getBook();
                book.setQuantity(book.getQuantity() + detail.getQuantity());
                book.setReserved(book.getReserved() - detail.getQuantity());
                bookRepository.save(book);
            }

            // Xóa các chi tiết đơn hàng
            orderDetailRepository.deleteAll(details);

            // Xóa đơn hàng
            orderRepository.delete(order);
        }
    }

    // Cập nhật trạng thái đã giao hàng mỗi ngày lúc 0h
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDeliveredStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Order> ordersToDeliver = orderRepository.findByDeliveredFalseAndDeliveryDateBefore(now);
        for (Order order : ordersToDeliver) {
            order.setDelivered(true);
            orderRepository.save(order);
        }
    }
}
