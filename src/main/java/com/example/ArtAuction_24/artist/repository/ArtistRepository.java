package com.example.ArtAuction_24.artist.repository;

import com.example.ArtAuction_24.artist.entity.Artist;
import com.example.ArtAuction_24.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Optional<Artist> findByAuthor(Member author);

}