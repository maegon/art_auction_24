package com.example.ArtAuction_24.domain.product.entity;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionProduct extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;  // 제품과의 연관 관계 추가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;  // 경매와의 연관 관계 추가

    public boolean isActive() {
        return this.auction != null && this.auction.getStatus() == AuctionStatus.ACTIVE;
    }

}
