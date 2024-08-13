package com.example.ArtAuction_24.domain.art.repository;

import com.example.ArtAuction_24.domain.art.entity.Art;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtRepository extends JpaRepository<Art, Integer> {
    Page<Art> findAll(Specification<Art> spec, Pageable pageable);
}
