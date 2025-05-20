package iuh.se.team.webbookstore_backend.controller;

import iuh.se.team.webbookstore_backend.dao.ShippingMethodRepository;
import iuh.se.team.webbookstore_backend.entities.ShippingMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shipping-methods")
@CrossOrigin(origins = "*")
public class ShippingMethodController {

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    @GetMapping
    public List<ShippingMethod> getAllShippingMethods() {
        return shippingMethodRepository.findAll();
    }
}
