package com.example.ArtAuction_24.domain.product.entity;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;

import com.example.ArtAuction_24.domain.review.entity.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionProduct extends BaseEntity {

    private String title;
    private String description;
    private String medium; //사용된 재료
    private String dimensions; //크기
    private int startingPrice; //시작 가격
    private int currentBid; // 현재 입찰가
    private LocalDateTime auctionStartDate; // 시작 일
    private String thumbnailImg; // 그림 이미지
    private String category; // 카테고리

    @ManyToOne
    private Artist artist;

    @ManyToOne
    private Auction auction;

    @OneToMany(mappedBy = "auctionProduct", cascade = CascadeType.REMOVE)
    private List<Review> reviewList;

    public void updateBid(int newBid) {
        if (newBid > currentBid) {
            currentBid = newBid;
        } else {
            throw new IllegalArgumentException("새 입찰가가 현재 입찰가보다 작습니다.");
        }
    }
}
