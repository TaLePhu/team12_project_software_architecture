package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "shipping-methods")
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Integer> {
}
