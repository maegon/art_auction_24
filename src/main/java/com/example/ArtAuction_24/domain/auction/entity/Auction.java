package com.example.ArtAuction_24.domain.auction.entity;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Auction extends BaseEntity { // 여러 제품을 경매에 올려 판매하는 이벤트.

    @Column(unique = true)
    private String name;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double StartingPrice;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status; // 경매 상태 (활성화, 종료, 취소)

    @ManyToMany
    @JoinTable(
            name = "AuctionProduct",
            joinColumns = @JoinColumn(name = "auction_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    public boolean isClosed() {
        return this.status == AuctionStatus.CLOSED;
    }

}
