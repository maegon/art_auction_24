package com.example.ArtAuction_24.product.repository;

import com.example.ArtAuction_24.product.entity.AuctionProduct;
import com.example.ArtAuction_24.product.entity.ExhibitionProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionProductRepository extends JpaRepository<ExhibitionProduct, Long> {

}
