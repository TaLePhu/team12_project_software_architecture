package iuh.se.team.webbookstore_backend.impl;

import iuh.se.team.webbookstore_backend.dao.*;
import iuh.se.team.webbookstore_backend.dto.OrderDTO;
import iuh.se.team.webbookstore_backend.dto.OrderDetailRequest;
import iuh.se.team.webbookstore_backend.dto.OrderRequest;
import iuh.se.team.webbookstore_backend.entities.*;
import iuh.se.team.webbookstore_backend.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ShippingMethodRepository shippingMethodRepository;
    private final BookRepository bookRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        // 1. Lấy các entity liên quan
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderRequest.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));
        ShippingMethod shippingMethod = shippingMethodRepository.findById(orderRequest.getShippingMethodId())
                .orElseThrow(() -> new RuntimeException("Shipping method not found"));

        // 2. Mapping OrderRequest -> Order
        Order order = new Order();
        order.setBillingAddress(orderRequest.getBillingAddress());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setTotalProductPrice(orderRequest.getTotalProductPrice());
        order.setShippingFee(orderRequest.getShippingFee());
        order.setPaymentFee(orderRequest.getPaymentFee());
        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setUser(user);
        order.setPaymentMethod(paymentMethod);
        order.setShippingMethod(shippingMethod);
        order.setEmail(orderRequest.getEmail());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setOrderDate(LocalDateTime.parse(orderRequest.getOrderDate()));
        order.setDeliveryDate(LocalDateTime.parse(orderRequest.getDeliveryDate()));

        // 3. Mapping OrderDetailRequest -> OrderDetail
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequest detailReq : orderRequest.getOrderDetails()) {
            Book book = bookRepository.findById(detailReq.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order); // gán order cha
            detail.setBook(book);
            detail.setQuantity(detailReq.getQuantity());
            detail.setSalePrice(detailReq.getSalePrice());
            orderDetails.add(detail);
        }
        order.setOrderDetails(orderDetails);

        // 4. Lưu order (cascade ALL sẽ tự lưu orderDetails)
        return orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllProjectedBy();
    }

    @Override
    public Optional<Order> getOrderById(int id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order updateOrder(int id, OrderRequest orderRequest) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        Order order = orderOpt.get();
        // Cập nhật các trường (tùy ý, ví dụ:)
        order.setBillingAddress(orderRequest.getBillingAddress());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setTotalProductPrice(orderRequest.getTotalProductPrice());
        order.setShippingFee(orderRequest.getShippingFee());
        order.setPaymentFee(orderRequest.getPaymentFee());
        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setEmail(orderRequest.getEmail());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setOrderDate(LocalDateTime.parse(orderRequest.getOrderDate()));
        order.setDeliveryDate(LocalDateTime.parse(orderRequest.getDeliveryDate()));
        // Có thể cập nhật paymentMethod, shippingMethod, user nếu muốn

        // Nếu muốn cập nhật orderDetails, bạn cần xóa cũ và thêm mới (tùy nghiệp vụ)
        // Ở đây chỉ cập nhật thông tin đơn hàng

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }
}