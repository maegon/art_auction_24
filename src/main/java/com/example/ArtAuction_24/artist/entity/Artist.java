package com.example.ArtAuction_24.artist.entity;

import com.example.ArtAuction_24.base.entity.BaseEntity;
import jakarta.persistence.*;
import com.example.ArtAuction_24.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
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

    private String korName;
    private String engName;
    private String birthDate;
    private String education;
    private String tel;
    private String mail;
    private String mailType;
    private String thumbnailImg;

    private String introduce;
    private String majorWork;
    private String title;
    private String content;

    @ManyToOne
    private Member author;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtistAdd> artistAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentAdd> contentAdds;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TitleAdd> titleAdds;

    public void setThumbnail(String thumbnailRelPath) {
        this.thumbnailImg = thumbnailRelPath;
    }
}
