package com.example.ArtAuction_24.product.repository;

import com.example.ArtAuction_24.answer.entity.Answer;
import com.example.ArtAuction_24.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
