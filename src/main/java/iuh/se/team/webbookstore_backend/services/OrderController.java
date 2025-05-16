

package iuh.se.team.webbookstore_backend.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import iuh.se.team.webbookstore_backend.dao.*;
import iuh.se.team.webbookstore_backend.dto.OrderDetailRequest;
import iuh.se.team.webbookstore_backend.dto.OrderRequest;
import iuh.se.team.webbookstore_backend.entities.*;
import iuh.se.team.webbookstore_backend.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ShippingMethodRepository shippingMethodRepository;
    private final BookRepository bookRepository;
    private final EmailServiceImpl emailServiceImpl;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket resolveBucket(String userKey) {
        return buckets.computeIfAbsent(userKey, key -> {
            Refill refill = Refill.greedy(5, Duration.ofMinutes(1)); // 5 lần/phút
            Bandwidth limit = Bandwidth.classic(5, refill);
            return Bucket.builder().addLimit(limit).build();
        });
    }

    public int getNextGuestUserId() {
        // Truy vấn để lấy userId lớn nhất từ bảng User (hoặc bảng chứa user)
        Integer maxUserId = userRepository.findMaxUserId(); // Giả sử bạn có một phương thức findMaxUserId() trong repository

        // Nếu không có user nào, bắt đầu từ 9000
        if (maxUserId == null) {
            return 9000;
        }

        // Nếu có userId, lấy giá trị lớn nhất và cộng thêm 1
        return maxUserId + 1;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {

            // Xác định user key cho rate limit (ví dụ: dùng userId hoặc email)
            String userKey;
            if (orderRequest.getUserId() != 0) {
                userKey = String.valueOf(orderRequest.getUserId());
            } else {
                // Khách lẻ thì có thể dùng email làm key, hoặc giả email
                userKey = orderRequest.getEmail() != null ? orderRequest.getEmail() : "guest-" + orderRequest.getPhoneNumber();
            }

            Bucket bucket = resolveBucket(userKey);

            if (!bucket.tryConsume(1)) {
                // Nếu vượt quá số lần gọi
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body("Bạn đã đặt hàng quá nhiều lần. Vui lòng thử lại sau 1 phút.");
            }

            User user;

            // Nếu userId = 0, tức là khách lẻ
            if (orderRequest.getUserId() == 0) {
                // Lấy userId lớn nhất hiện tại và cộng thêm 1
                int nextGuestUserId = getNextGuestUserId();  // Lấy userId tăng dần từ 9000
                user = new User();  // Tạo một đối tượng User mới (khách lẻ)
                user.setUserId(nextGuestUserId);  // Gán userId cho khách lẻ
                user.setLastName(orderRequest.getLastName());
                user.setFirstName(orderRequest.getFirstName());
                user.setEmail("guest" + nextGuestUserId + "@example.com");  // Tạo email giả cho khách lẻ
                user.setPhoneNumber("000000000" + nextGuestUserId);  // Tạo số điện thoại gi
                user.setActivated(true);  // Đánh dấu là đã kích hoạt
                user.setPassword("000000");  // Tạo mật khẩu giả cho khách lẻ
            } else {
                // Nếu có userId, lấy thông tin user từ database
                user = userRepository.findById(orderRequest.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }

            // Lấy phương thức thanh toán và vận chuyển
            Optional<PaymentMethod> paymentMethodOpt = paymentMethodRepository.findById(orderRequest.getPaymentMethodId());
            Optional<ShippingMethod> shippingMethodOpt = shippingMethodRepository.findById(orderRequest.getShippingMethodId());

            if (paymentMethodOpt.isEmpty() || shippingMethodOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Payment or Shipping method not found");
            }

            // Tạo order
            Order order = new Order();
            order.setBillingAddress(orderRequest.getBillingAddress());
            order.setShippingAddress(orderRequest.getShippingAddress());
            order.setTotalProductPrice(orderRequest.getTotalProductPrice());
            order.setShippingFee(orderRequest.getShippingFee());
            order.setPaymentFee(orderRequest.getPaymentFee());
            order.setTotalPrice(orderRequest.getTotalPrice());
            order.setCreatedDate(new Date(System.currentTimeMillis()));
            order.setUser(user);  // Gán đối tượng User (hoặc khách lẻ) cho đơn hàng
            order.setPaymentMethod(paymentMethodOpt.get());
            order.setShippingMethod(shippingMethodOpt.get());
            order.setEmail(orderRequest.getEmail());
            order.setPhoneNumber(orderRequest.getPhoneNumber());

            Order savedOrder = orderRepository.save(order);

            String token = UUID.randomUUID().toString();
            order.setConfirmationToken(token); // nếu có trường token
            orderRepository.save(order); // cập nhật lại token

            String confirmLink = "http://localhost:8080/api/orders/confirm?orderId=" + order.getOrderId() + "&token=" + token;

            String subject = "Xác nhận đơn hàng #" + order.getOrderId();
            String body = "<p>Chào " + user.getFirstName() + ",</p>"
                    + "<p>Bạn vừa đặt một đơn hàng tại cửa hàng của chúng tôi.</p>"
                    + "<p>Vui lòng xác nhận đơn hàng bằng cách nhấn vào liên kết sau:</p>"
                    + "<a href=\"" + confirmLink + "\">Xác nhận đơn hàng</a>";

            emailServiceImpl.sendEmail(
                    "lephu18062@gmail.com", // From
                    order.getEmail(),       // To
                    subject,
                    body,
                    true
            );

            // Lưu chi tiết đơn hàng
            for (OrderDetailRequest detailReq : orderRequest.getOrderDetails()) {
                Optional<Book> bookOpt = bookRepository.findById(detailReq.getBookId());
                if (bookOpt.isEmpty()) {
                    continue; // hoặc throw exception tùy cách xử lý
                }

                OrderDetail detail = new OrderDetail();
                detail.setOrder(savedOrder);
                detail.setBook(bookOpt.get());
                detail.setQuantity(detailReq.getQuantity());
                detail.setSalePrice(detailReq.getSalePrice());
                orderDetailRepository.save(detail);
            }

            return ResponseEntity.ok("Order placed successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating order: " + e.getMessage());
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmOrder(@RequestParam int orderId, @RequestParam String token) {

        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Order not found");
        }

        Order order = orderOpt.get();
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);

        if (!order.getConfirmationToken().equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Đánh dấu đã xác nhận
        order.setConfirmed(true);
        orderRepository.save(order);

        StringBuilder productListHtml = new StringBuilder();
        productListHtml.append("<ul>");

        for (OrderDetail detail : orderDetails) {
            String bookTitle = detail.getBook().getBookName();
            int quantity = detail.getQuantity();
            double price = detail.getSalePrice();

            productListHtml.append("<li>")
                    .append(bookTitle)
                    .append(" - SL: ").append(quantity)
                    .append(" - Giá: ").append(String.format("%.0f", price))
                    .append("</li>");
        }

        productListHtml.append("</ul>");

        // Gửi email thông tin đơn hàng
        String detail = "<p><strong>Thông tin đơn hàng:</strong></p>"
                + "<p>Họ tên: " + order.getUser().getFirstName() + " " + order.getUser().getLastName() + "<br>"
                + "Địa chỉ giao hàng: " + order.getShippingAddress() + "<br>"
                + "Số điện thoại: " + order.getPhoneNumber() + "<br>"
                + "Email: " + order.getEmail() + "</p>"
                + "<p><strong>Danh sách sản phẩm:</strong></p>"
                + productListHtml.toString()
                + "<p><strong>Tổng tiền: " + String.format("%.0f", order.getTotalPrice()) + " VND</strong></p>";

        emailServiceImpl.sendEmail(
                "lephu18062@gmail.com",
                order.getEmail(),
                "Đơn hàng #" + order.getOrderId() + " đã được xác nhận",
                "<p>" + detail + "</p>",
                true
        );

        // Redirect về trang web (FE)
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "http://localhost:3000/order-confirm-success") // FE URL
                .build();
    }

}
