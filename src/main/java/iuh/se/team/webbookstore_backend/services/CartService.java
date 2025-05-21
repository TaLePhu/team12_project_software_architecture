package iuh.se.team.webbookstore_backend.services;

import iuh.se.team.webbookstore_backend.dto.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public CartService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getKey(int userId) {
        return "cart:user:" + userId;
    }

    public List<CartItem> getCart(int userId) {
        Object data = redisTemplate.opsForValue().get(getKey(userId));
        if (data instanceof List<?>) {
            return (List<CartItem>) data;
        }
        return new ArrayList<>();
    }

    public void saveCart(int userId, List<CartItem> cartItems) {
        redisTemplate.opsForValue().set(getKey(userId), cartItems);
        // Đặt thời gian hết hạn là 30 ngày
        redisTemplate.expire(getKey(userId), Duration.ofDays(30));
    }

    public void addToCart(int userId, CartItem item) {
        List<CartItem> cart = getCart(userId);
        Optional<CartItem> existing = cart.stream()
                .filter(i -> i.getBookId() == item.getBookId())
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
        } else {
            cart.add(0, item);
        }
        saveCart(userId, cart);
    }

    public void removeFromCart(int userId, int bookId) {
        List<CartItem> cart = getCart(userId);
        cart.removeIf(i -> i.getBookId() == bookId);
        saveCart(userId, cart);
    }

    public void updateQuantity(int userId, int bookId, int quantity) {
        List<CartItem> cart = getCart(userId);
        for (CartItem item : cart) {
            if (item.getBookId() == bookId) {
                item.setQuantity(quantity);
                break;
            }
        }
        saveCart(userId, cart);
    }

    public void clearCart(int userId) {
        redisTemplate.delete(getKey(userId));
    }

    public void removeMultipleFromCart(int userId, List<Integer> bookIds) {
        List<CartItem> cart = getCart(userId);
        cart.removeIf(item -> bookIds.contains(item.getBookId()));
        saveCart(userId, cart);
    }
}