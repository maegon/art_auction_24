package com.example.ArtAuction_24.domain.deliver.service;

import com.example.ArtAuction_24.domain.deliver.deliverRepository.DeliveryRepository;
import com.example.ArtAuction_24.domain.deliver.entity.Delivery;
import com.example.ArtAuction_24.domain.deliver.entity.DeliveryStatus;
import com.example.ArtAuction_24.domain.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery createDelivery(Order order, String trackingUrl) {
        LocalDateTime completedDate = order.getCreateDate().plusDays(5);

        Delivery delivery = Delivery.builder()
                .order(order)
                .startDate(LocalDateTime.now())
                .completedDate(completedDate)
                .trackingUrl(trackingUrl)
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();

        try {
            return deliveryRepository.save(delivery);
        } catch (Exception e) {
            // 예외 처리 및 로깅
            throw new RuntimeException("배송 생성 중 오류 발생", e);
        }
    }

    @Transactional
    public void updateDeliveryStatus(Delivery delivery, DeliveryStatus status) {
        delivery.setDeliveryStatus(status);
        if (status == DeliveryStatus.COMPLETED) {
            delivery.setCompletedDate(LocalDateTime.now());
        }

        try {
            deliveryRepository.save(delivery);
        } catch (Exception e) {
            // 예외 처리 및 로깅
            throw new RuntimeException("배송 상태 업데이트 중 오류 발생", e);
        }
    }

    public Delivery getDeliveryByOrder(Order order) {
        return deliveryRepository.findByOrder(order);
    }
}
