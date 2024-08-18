package com.example.ArtAuction_24.domain.artist.entity;

import com.example.ArtAuction_24.domain.artist.repository.UnitContentAddRepository;
import com.example.ArtAuction_24.domain.product.entity.Product;
import jakarta.persistence.*;
import com.example.ArtAuction_24.domain.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String korName;
    @Setter
    @Getter
    private String engName;
    @Setter
    @Getter
    private String birthDate;
    @Setter
    @Getter
    private String tel;
    @Setter
    @Getter
    private String mail;
    @Setter
    @Getter
    private String mailType;
    private String thumbnailImg;

    private String introduce;
    private String majorWork;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author; // 변경된 필드

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;


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

    public void setThumbnail(String thumbnailRelPath) {
        this.thumbnailImg = thumbnailRelPath;
    }

}
