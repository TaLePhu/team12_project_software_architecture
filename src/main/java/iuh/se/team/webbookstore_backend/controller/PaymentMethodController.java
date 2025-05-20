package iuh.se.team.webbookstore_backend.controller;
import iuh.se.team.webbookstore_backend.dao.PaymentMethodRepository;
import iuh.se.team.webbookstore_backend.entities.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@CrossOrigin(origins = "*")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @GetMapping
    public List<PaymentMethod> getAll() {
        return paymentMethodRepository.findAll();
    }
}
