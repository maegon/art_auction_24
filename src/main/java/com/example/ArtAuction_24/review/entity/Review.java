package com.example.ArtAuction_24.review.entity;

import com.example.ArtAuction_24.base.entity.BaseEntity;
import com.example.ArtAuction_24.member.entity.Member;
import com.example.ArtAuction_24.product.entity.AuctionProduct;
import com.example.ArtAuction_24.product.entity.ExhibitionProduct;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

    private String content; // 리뷰 내용

    @ManyToOne
    private Member member; // 리뷰를 작성한 회원

    @ManyToOne
    private AuctionProduct auctionProduct; // 리뷰가 작성된 제품

    @ManyToOne
    private ExhibitionProduct exhibitionProduct; // 리뷰가 작성된 제품
}
