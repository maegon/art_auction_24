package com.example.ArtAuction_24.domain.art.repository;

import com.example.ArtAuction_24.domain.art.entity.Art;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtRepository extends JpaRepository<Art, Integer> {
    Optional<Art> findByAuthor(Member author);
    List<Art> findByArtist(Artist artist);
}
