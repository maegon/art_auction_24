package com.example.ArtAuction_24.domain.product.service;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.entity.LikeProduct;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.repository.AuctionProductRepository;
import com.example.ArtAuction_24.domain.product.repository.LikeProductRepository;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AuctionProductRepository auctionProductRepository;
    private final MemberRepository memberRepository;
    private final LikeProductRepository likeProductRepository;

    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;



    public Page<Product> getProductsWithSorting(String keyword, Pageable pageable, String sortOption, Boolean auction) {
        Sort sort = switch (sortOption) {
            case "price-asc" -> Sort.by("currentBid").ascending();
            case "price-desc" -> Sort.by("currentBid").descending();
            case "latest" -> Sort.by("createDate").descending();
            default -> Sort.by("createDate").descending();
        };

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        if (keyword != null && !keyword.isEmpty()) {
            if (Boolean.TRUE.equals(auction)) {
                return productRepository.findByKeywordAndAuctionActive(keyword, sortedPageable);
            } else {
                return productRepository.findByTitleContainingOrArtistKorNameContainingOrArtistEngNameContaining(keyword, keyword, keyword, sortedPageable);
            }
        } else {
            if (Boolean.TRUE.equals(auction)) {
                return productRepository.findByAuctionActive(sortedPageable);
            } else {
                return productRepository.findAll(sortedPageable);
            }
        }
    }

    public Product create(String title, String description, String medium, String dimensions,
                          BigDecimal startingPrice, LocalDateTime auctionStartDate,
                          String thumbnailImg, String category, Artist artist) {

        // 파일 경로를 설정합니다.
        String thumbnailRelPath = "image/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

        // 디렉토리 생성
        File parentDir = thumbnailFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new RuntimeException("디렉토리를 생성할 수 없습니다: " + parentDir.getAbsolutePath());
            }
        }

        // Product 객체를 생성합니다.
        Product p = Product.builder()
                .title(title)
                .description(description)
                .medium(medium)
                .dimensions(dimensions)
                .startingPrice(startingPrice)
                .currentBid(startingPrice)  // startingPrice와 동일하게 설정
                .auctionStartDate(auctionStartDate)  // 매개변수로 받은 auctionStartDate 사용
                .thumbnailImg(thumbnailImg)
                .category(category)
                .artist(artist)
                .build();

        // Product를 저장합니다.
        productRepository.save(p);

        return p;
    }


    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getList() {
        return productRepository.findAll();
    }

    public List<Product> findByKeyword(String keyword) {
        return productRepository.findByKeyword(keyword);
    }

    public List<Product> findAllProductOrderByCreateDateDesc() {
        return productRepository.findAllByOrderByCreateDateDesc();
    }

    public Product getTopProductByView() {
        return productRepository.findTopByOrderByViewDesc();
    }

    @Transactional
    public void incrementViews(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setView(product.getView() + 1);
        productRepository.save(product);
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByTitleContainingOrArtistKorNameContainingOrArtistEngNameContaining(keyword, keyword, keyword, pageable);
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<Product> findAll() {
        return productRepository.findAllWithAuction(); // 경매 정보와 함께 모든 제품을 가져옴
    }

    public AuctionProduct getAuctionProduct(Long productId) {
        return auctionProductRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("AuctionProduct not found for product id: " + productId));
    }



    public List<Product> findProductsByActiveAuctions() {
        return productRepository.findProductsByActiveAuctions();
    }

    // ACTIVE 상태의 경매에 포함된 제품을 찾기 위한 메소드
    public List<Product> findProductsByAuctionStatus(AuctionStatus status) {
        return productRepository.findProductsByAuctionStatus(status);
    }

    // 찜 추가 또는 취소
    public void toggleLike(Long productId, Long memberId) {
        Optional<LikeProduct> likeProductOpt = likeProductRepository.findByProductIdAndMemberId(productId, memberId);

        if (likeProductOpt.isPresent()) {
            // 이미 찜이 존재하면 삭제 (찜 취소)
            likeProductRepository.delete(likeProductOpt.get());
        } else {
            // 찜이 없으면 추가
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

            LikeProduct likeProduct = new LikeProduct();
            likeProduct.setProduct(product);
            likeProduct.setMember(member);

            likeProductRepository.save(likeProduct);

        }
    }

    public Set<Long> getLikedProductIdsByMember(Long memberId) {
        return likeProductRepository.findByMemberId(memberId).stream()
                .map(likeProduct -> likeProduct.getProduct().getId())
                .collect(Collectors.toSet());
    }



    public List<LikeProduct> getLikeProduct(Member member) {
        return likeProductRepository.findByMember(member);
    }

    // ID로 상품 찾기
    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
    }

    // 상품의 현재 가격 가져오기
    public BigDecimal getCurrentPrice(Long productId) {
        Product product = findById(productId);
        return product.getCurrentPrice();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAllByAuction(Auction auction) {
        return productRepository.findAllByAuctions(auction);

    }

    // 재원 추가
    public List<Product> getProductList() {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        return productRepository.findAll();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findByWinningBidderIsNull();
    }

    // 특정 제품에 대해 가장 최신의 경매 상품을 반환
    public Optional<AuctionProduct> getLatestAuctionProduct(Long productId) {
        return auctionProductRepository.findTopByProductIdOrderByAuctionCreateDateDesc(productId);
    }

}
