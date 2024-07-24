package com.example.ArtAuction_24.artist.repository;

import com.example.ArtAuction_24.artist.entity.Artist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    @Transactional
    // @Modifying // 만약 아래 쿼리가 SELECT가 아니라면 이걸 붙여야 한다.
    @Modifying
    // nativeQuery = true 여야 MySQL 쿼리 문법 사용 가능
    @Query(value="ALTER TABLE artist AUTO_INCREMENT = 1", nativeQuery = true)
    void clearAutoIncrement();
}
