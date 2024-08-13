package com.example.ArtAuction_24.domain.artist.repository;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.artist.entity.ContentAdd;
import com.example.ArtAuction_24.domain.artist.entity.TechniqueContentAdd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechniqueContentAddRepository extends JpaRepository<TechniqueContentAdd, Long> {
    void deleteAllByArtist(Artist artist);  // 특정 아티스트의 모든 ContentAdd 삭제
}

