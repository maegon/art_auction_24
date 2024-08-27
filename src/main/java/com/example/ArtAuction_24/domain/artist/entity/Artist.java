package com.example.ArtAuction_24.domain.artist.entity;


import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import jakarta.persistence.*;
import com.example.ArtAuction_24.domain.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Artist extends BaseEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Setter
    @Getter
    private String korName;
    @Setter
    @Getter
    private String engName;
    @Setter
    @Getter
    private String birthDate;
    private String thumbnailImg;

    private String proofFilePath;

    private String introduce;
    private String majorWork;

    @Column(nullable = false)
    private Long balance = 0L; // 기본값을 0으로 설정, 가지고 있는 소지금

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    private boolean isApproved;


    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ArtistAdd> artistAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ContentAdd> contentAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TitleContentAdd> titleContentAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<YearContentAdd> yearContentAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<WidthContentAdd> widthContentAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<HeightContentAdd> heightContentAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UnitContentAdd> unitContentAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TechniqueContentAdd> techniqueContentAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TitleAdd> titleAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.REMOVE)
    private List<Product> product;

    @ManyToMany(mappedBy = "favoriteArtists")
    private Set<Member> favoritedBy = new HashSet<>();

    public void setThumbnail(String thumbnailRelPath) {
        this.thumbnailImg = thumbnailRelPath;
    }

}