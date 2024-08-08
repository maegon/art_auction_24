package com.example.ArtAuction_24.domain.product.service;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.repository.AuctionProductRepository;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AuctionProductRepository auctionProductRepository;

    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    public Page<Product> getProductsWithSorting(String keyword, Pageable pageable, String sortOption, Boolean auction) {
        Sort sort = switch (sortOption) {
            case "price-asc" -> Sort.by("startingPrice").ascending();
            case "price-desc" -> Sort.by("startingPrice").descending();
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

    public void create(String title, String description, String medium, String dimensions,
                       BigDecimal startingPrice, BigDecimal currentBid,
                       LocalDateTime auctionStartDate, String thumbnailImg, String category, Artist artist) {
        Product p = Product.builder()
                .title(title)
                .description(description)
                .medium(medium)
                .dimensions(dimensions)
                .startingPrice(startingPrice)
                .currentBid(currentBid)  // startingPrice와 동일하게 설정
                .auctionStartDate(auctionStartDate)
                .thumbnailImg(thumbnailImg)
                .category(category)
                .artist(artist)
                .build();
        productRepository.save(p);
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

    public AuctionProduct getAuctionProduct(Long id) {
        return auctionProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuctionProduct not found"));
    }

    public List<Product> findProductsByActiveAuctions() {
        return productRepository.findProductsByActiveAuctions();
    }
}
