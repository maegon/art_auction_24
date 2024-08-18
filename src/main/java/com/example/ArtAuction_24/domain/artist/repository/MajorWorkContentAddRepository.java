package com.example.ArtAuction_24.domain.artist.repository;

import com.example.ArtAuction_24.domain.artist.entity.MajorWorkContentAdd;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorWorkContentAddRepository extends JpaRepository<MajorWorkContentAdd, Long> {
    void deleteAllByArtist(Artist artist);

    // 추가적인 쿼리 메서드를 정의할 수 있습니다
}
