package com.example.ArtAuction_24.domain.product.repository;

import com.example.ArtAuction_24.domain.product.entity.ExhibitionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionProductRepository extends JpaRepository<ExhibitionProduct, Long> {

}
