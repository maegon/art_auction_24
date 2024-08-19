package com.example.ArtAuction_24.domain.order.entity;

public enum OrderStatus {
    PENDING("주문 대기 중"),      // 주문 대기 중
    CONFIRMED("주문 확정"),    // 주문 확정
    SHIPPED("배송 중"),      // 배송 중
    DELIVERED("배송 완료"),    // 배송 완료
    CANCELLED("주문 취소");     // 주문 취소

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
