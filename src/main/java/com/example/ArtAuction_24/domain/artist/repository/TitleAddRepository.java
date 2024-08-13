package com.example.ArtAuction_24.domain.artist.repository;

import com.example.ArtAuction_24.domain.artist.entity.TitleAdd;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleAddRepository extends JpaRepository<TitleAdd, Long> {
    void deleteAllByArtist(Artist artist);  // 특정 아티스트의 모든 TitleAdd 삭제
}

