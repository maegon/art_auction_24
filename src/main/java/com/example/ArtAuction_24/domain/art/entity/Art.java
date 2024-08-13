package com.example.ArtAuction_24.domain.art.entity;

import com.example.ArtAuction_24.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Art {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String thumbnailImg;
    @Setter
    @Getter
    private String korTitle;
    @Setter
    @Getter
    private String engTitle;
    private String artist;
    private String width;
    private String height;
    private String unit;
    private String technique;
    private String price;
    private String place;
    private String artIntroduction;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author; // 변경된 필드
}
