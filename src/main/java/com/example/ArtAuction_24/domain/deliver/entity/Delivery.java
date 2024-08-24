package com.example.ArtAuction_24.domain.deliver.entity;


import com.example.ArtAuction_24.domain.order.entity.Order;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime completedDate;

    private String trackingUrl;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}