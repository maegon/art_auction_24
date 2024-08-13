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
public class TitleAdd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ensure there is a no-argument setter for title
    // Ensure there is a no-argument getter for title
    @Setter
    @Getter
    private String title; // Ensure this field is defined

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
    @Setter
    private String content;

}
