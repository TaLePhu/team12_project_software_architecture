
//    @PostMapping
//    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
//        try {
//            // Lấy thông tin người dùng
//            Optional<User> userOpt = userRepository.findById(orderRequest.getUserId());
//            if (userOpt.isEmpty()) {
//                return ResponseEntity.badRequest().body("User not found");
//            }
//
//
//            // Lấy phương thức thanh toán và vận chuyển
//            Optional<PaymentMethod> paymentMethodOpt = paymentMethodRepository.findById(orderRequest.getPaymentMethodId());
//            Optional<ShippingMethod> shippingMethodOpt = shippingMethodRepository.findById(orderRequest.getShippingMethodId());
//
//            if (paymentMethodOpt.isEmpty() || shippingMethodOpt.isEmpty()) {
//                return ResponseEntity.badRequest().body("Payment or Shipping method not found");
//            }
//
//            // Tạo order
//            Order order = new Order();
//            order.setBillingAddress(orderRequest.getBillingAddress());
//            order.setShippingAddress(orderRequest.getShippingAddress());
//            order.setTotalProductPrice(orderRequest.getTotalProductPrice());
//            order.setShippingFee(orderRequest.getShippingFee());
//            order.setPaymentFee(orderRequest.getPaymentFee());
//            order.setTotalPrice(orderRequest.getTotalPrice());
//            order.setCreatedDate(new Date(System.currentTimeMillis()));
//            order.setUser(userOpt.get());
//            order.setPaymentMethod(paymentMethodOpt.get());
//            order.setShippingMethod(shippingMethodOpt.get());
//
//
//            Order savedOrder = orderRepository.save(order);
//
//            // Lưu chi tiết đơn hàng
//            for (OrderDetailRequest detailReq : orderRequest.getOrderDetails()) {
//                Optional<Book> bookOpt = bookRepository.findById(detailReq.getBookId());
//                if (bookOpt.isEmpty()) {
//                    continue; // hoặc throw exception tùy cách xử lý
//                }
//
//                OrderDetail detail = new OrderDetail();
//                detail.setOrder(savedOrder);
//                detail.setBook(bookOpt.get());
//                detail.setQuantity(detailReq.getQuantity());
//                detail.setSalePrice(detailReq.getSalePrice());
//                orderDetailRepository.save(detail);
//            }
//
//            return ResponseEntity.ok("Order placed successfully");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error while creating order: " + e.getMessage());
//        }
//    }
package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dao.*;
import iuh.se.team.webbookstore_backend.dto.OrderDetailRequest;
import iuh.se.team.webbookstore_backend.dto.OrderRequest;
import iuh.se.team.webbookstore_backend.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Optional;

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
            User user;

            // Nếu userId = 0, tức là khách lẻ
            if (orderRequest.getUserId() == 0) {
                // Lấy userId lớn nhất hiện tại và cộng thêm 1
                int nextGuestUserId = getNextGuestUserId();  // Lấy userId tăng dần từ 9000
                user = new User();  // Tạo một đối tượng User mới (khách lẻ)
                user.setUserId(nextGuestUserId);  // Gán userId cho khách lẻ
                user.setLastName(" " + nextGuestUserId);
                user.setFirstName("Guest");
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

            Order savedOrder = orderRepository.save(order);

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

}
