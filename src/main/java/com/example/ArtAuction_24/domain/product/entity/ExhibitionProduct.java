package com.example.ArtAuction_24.domain.product.entity;

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

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionProduct extends BaseEntity {

    private String title;
    private String description;
    private String medium; // 사용된 재료
    private String dimensions; // 크기
    private String thumbnailImg; // 그림 이미지
    private String category; // 카테고리

    @ManyToOne
    private Artist artist;

    @OneToMany(mappedBy = "exhibitionProduct", cascade = CascadeType.REMOVE)
    private List<Review> reviewList;
}
