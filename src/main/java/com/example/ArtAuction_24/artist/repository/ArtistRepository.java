package com.example.ArtAuction_24.artist.repository;

import com.example.ArtAuction_24.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

}