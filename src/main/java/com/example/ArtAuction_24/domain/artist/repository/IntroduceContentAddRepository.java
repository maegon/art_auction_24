package com.example.ArtAuction_24.domain.artist.repository;

import com.example.ArtAuction_24.domain.artist.entity.IntroduceContentAdd;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroduceContentAddRepository extends JpaRepository<IntroduceContentAdd, Long> {
    void deleteAllByArtist(Artist artist);
}
