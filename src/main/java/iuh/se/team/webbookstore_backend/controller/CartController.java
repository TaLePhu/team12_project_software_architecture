package iuh.se.team.webbookstore_backend.controller;

import iuh.se.team.webbookstore_backend.dto.CartItem;
import iuh.se.team.webbookstore_backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public List<CartItem> getCart(@RequestParam int userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/add")
    public void addToCart(@RequestParam int userId, @RequestBody CartItem item) {
        cartService.addToCart(userId, item);
    }

    @PutMapping("/update")
    public void updateQuantity(@RequestParam int userId,
                               @RequestParam int bookId,
                               @RequestParam int quantity) {
        cartService.updateQuantity(userId, bookId, quantity);
    }

    @DeleteMapping("/remove")
    public void removeItem(@RequestParam int userId, @RequestParam int bookId) {
        cartService.removeFromCart(userId, bookId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@RequestParam int userId) {
        cartService.clearCart(userId);
    }

    @DeleteMapping("/remove-multiple")
    public void removeMultipleItems(@RequestParam int userId, @RequestBody List<Integer> bookIds) {
        cartService.removeMultipleFromCart(userId, bookIds);
    }
}
