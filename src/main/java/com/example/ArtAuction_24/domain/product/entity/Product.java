package com.example.ArtAuction_24.domain.product.entity;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.member.entity.Member;


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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    private String title;
    private String description;
    private String medium; //사용된 재료
    private String dimensions; //크기
    private BigDecimal startingPrice; //시작 가격
    private BigDecimal currentBid; // 현재 입찰가
    private LocalDateTime auctionStartDate; // 시작 일
    private String thumbnailImg; // 그림 이미지
    private String category; // 카테고리
    private int view;

    @ManyToOne
    private Artist artist;

    @ManyToMany(mappedBy = "products")
    private Set<Auction> auctions = new HashSet<>();



    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<LikeProduct> likeProductList;

    @OneToMany(mappedBy = "product")
    private List<Bid> bids;

    @OneToMany(mappedBy = "product")
    private List<AuctionProduct> auctionProductList;



    private transient String formattedCurrentBid; //직렬화
    private transient String formattedStartingPrice; //직렬화

    @ManyToOne
    @JoinColumn(name = "winning_bidder_id")
    private Member winningBidder; //제품 낙찰자

    @Column(name = "previous_bid")
    private BigDecimal previousBid; //원래 입찰가 백업


    @PostLoad
    private void postLoad() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        this.formattedCurrentBid = formatter.format(this.currentBid);
        this.formattedStartingPrice = formatter.format(this.startingPrice);
    }

    public void updateBid(BigDecimal newBid) {
        if (newBid.compareTo(currentBid) > 0) {
            currentBid = newBid;
        } else {
            throw new IllegalArgumentException("새 입찰가가 현재 입찰가보다 작습니다.");
        }
    }


    //지우지마시오 myPage 에서 쓰는중임
    public LocalDateTime getAuctionEndDate() {
        // Auction이 여러 개일 수 있으므로, 경매가 있는 경우 가장 최근의 경매를 반환
        return auctions.stream()
                .filter(auction -> auction.getEndDate() != null)
                .map(Auction::getEndDate)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    // 제일 최근의 AuctionProduct 객체를 반환하는 메소드
    public AuctionProduct getLatestAuctionProduct() {
        return auctionProductList.stream()
                .filter(auctionProduct -> auctionProduct.getAuction() != null)
                .max((a1, a2) -> a1.getId().compareTo(a2.getId()))
                .orElse(null);
    }





    // 현재 입찰가를 반환하는 메소드
    public BigDecimal getCurrentPrice() {
        return currentBid;
    }



}
