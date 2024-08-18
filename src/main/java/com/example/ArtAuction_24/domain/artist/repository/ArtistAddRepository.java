package com.example.ArtAuction_24.domain.artist.repository;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.artist.entity.ArtistAdd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistAddRepository extends JpaRepository<ArtistAdd, Integer> {
<<<<<<< HEAD
<<<<<<< HEAD
    void deleteAllByArtist(Artist artist);
=======

=======
>>>>>>> 345c4c2 (aa)
    void deleteByArtist(Artist artist);
>>>>>>> 15f227a (aa)
}
