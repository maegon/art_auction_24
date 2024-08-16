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
public class TitleContentAdd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String content;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
}