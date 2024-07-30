package com.example.ArtAuction_24.artist.repository;

import com.example.ArtAuction_24.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    @Query("SELECT a FROM Artist a WHERE LOWER(a.korName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Artist> findByKeyword(@Param("keyword") String keyword);

}