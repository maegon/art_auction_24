package com.example.ArtAuction_24.domain.deliver.deliverRepository;

import com.example.ArtAuction_24.domain.deliver.entity.Delivery;
import com.example.ArtAuction_24.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Delivery findByOrder(Order order);
}
