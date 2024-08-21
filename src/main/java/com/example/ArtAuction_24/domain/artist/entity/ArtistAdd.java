package com.example.ArtAuction_24.domain.artist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(exclude = {"artist"})
public class ArtistAdd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ensure there is a no-argument getter for content
    @Setter
    @Getter
    private String content; // Ensure this field is defined

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    // 생성자 추가
    public ArtistAdd(Artist artist, String content) {
        this.artist = artist;
        this.content = content;
    }


}
