package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "payment-methods")
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
}
