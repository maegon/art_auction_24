package com.example.ArtAuction_24.domain.deliver.entity;

public enum DeliveryStatus {
    PENDING("배송 준비 중"),
    IN_TRANSIT("배송 중"),
    COMPLETED("배송 완료");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}