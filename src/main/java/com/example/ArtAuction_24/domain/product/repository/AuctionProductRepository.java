package com.example.ArtAuction_24.domain.product.repository;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.form.ProductForm;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuctionProductRepository extends JpaRepository<AuctionProduct, Long> {
    List<AuctionProduct> findByAuctionId(Long auctionId);

    Optional<AuctionProduct> findByProductId(Long productId);

    // 특정 제품에 대해 가장 최근 경매를 반환하는 쿼리 메서드
    Optional<AuctionProduct> findTopByProductIdOrderByAuctionCreateDateDesc(Long productId);

    // product_id로 경매 상품 존재 여부 확인
    boolean existsByProductId(long productId);

    // product_id로 경매 상품 삭제
    void deleteByProductId(long productId);
}
