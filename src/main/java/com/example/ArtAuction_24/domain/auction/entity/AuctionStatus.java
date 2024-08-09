package com.example.ArtAuction_24.domain.auction.entity;

public enum AuctionStatus {
    ACTIVE,    // 경매가 활성화된 상태
    CLOSED,    // 경매가 종료된 상태
    CANCELLED, // 경매가 취소된 상태
    SCHEDULED  // 경매가 예정된 상태
}