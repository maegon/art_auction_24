package com.example.ArtAuction_24.domain.auction.service;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.auction.form.AuctionForm;
import com.example.ArtAuction_24.domain.auction.repository.AuctionRepository;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.bid.service.BidService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.notification.service.NotificationService;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.repository.AuctionProductRepository;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import io.micrometer.common.KeyValues;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final BidService bidService;
    private final MemberService memberService;
    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(AuctionService.class);

    public Auction create(String name, LocalDateTime startDate, LocalDateTime endDate, List<Long> productIds) {
        // 중복 경매 이름 체크
        if (auctionRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 경매 이름입니다.");
        }

        List<Product> products = productRepository.findAllById(productIds);
        Set<Product> productSet = new HashSet<>(products);

        AuctionStatus auctionStatus = startDate.isAfter(LocalDateTime.now()) ? AuctionStatus.SCHEDULED : AuctionStatus.ACTIVE;


        Auction auction = Auction.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .status(auctionStatus)
                .products(productSet)
                .build();

        return auctionRepository.save(auction);
    }

    @Scheduled(fixedDelay = 10000) // 10초마다 실행
    public void updateAuctionStatuses() {
        LocalDateTime now = LocalDateTime.now();

        // 만료된 경매를 종료합니다.
        List<Auction> expiredAuctions = auctionRepository.findByEndDateBeforeAndStatus(now, AuctionStatus.ACTIVE);
        for (Auction auction : expiredAuctions) {
            auction.setStatus(AuctionStatus.CLOSED);
            auctionRepository.save(auction);
            logger.info("Auction with ID {} has been closed.", auction.getId());

            // 경매 종료 후 최종 입찰자의 잔액 차감
            bidService.finalizeAuction(auction.getId());  // 경매 ID로 호출

        }

        // 예약된 경매를 활성화합니다.
        List<Auction> scheduledAuctions = auctionRepository.findByStartDateBeforeAndStatus(now, AuctionStatus.SCHEDULED);
        for (Auction auction : scheduledAuctions) {
            auction.setStatus(AuctionStatus.ACTIVE);
            auctionRepository.save(auction);
        }
    }

    public List<Auction> findAll() {
        return auctionRepository.findAll();
    }

    public List<Auction> getAuctionsByStatus(AuctionStatus status) {
        return auctionRepository.findByStatus(status);
    }

    public Page<Product> getProductsWithFilteringAndSorting(String keyword, String auctionName, Pageable pageable, String sort) {
        Sort sortOrder = switch (sort) {
            case "price-asc" -> Sort.by("startingPrice").ascending();
            case "price-desc" -> Sort.by("startingPrice").descending();
            case "latest" -> Sort.by("auctionStartDate").descending();
            default -> Sort.by("auctionStartDate").descending();
        };

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);

        Page<Product> products = productRepository.findByActiveAuctionsAndFilter(keyword, auctionName, sortedPageable);

        return products;
    }

    public List<String> getDistinctAuctionNames() {
        return auctionRepository.findDistinctAuctionNames();
    }

    // 진행중인 경매(ACTIVE) 목록을 반환하는 메소드
    public List<Auction> findActiveAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.ACTIVE);
    }

    // 예정된 경매(SCHEDULED) 목록을 반환하는 메소드
    public List<Auction> getScheduledAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.SCHEDULED);
    }

    public Auction getScheduledAuctionById(Long id) {
        Optional<Auction> optionalAuction = auctionRepository.findById(id);
        if (optionalAuction.isPresent() && optionalAuction.get().getStatus() == AuctionStatus.SCHEDULED) {
            return optionalAuction.get();
        }
        return null; // Return null if the auction is not found or not scheduled
    }


    public List<Auction> getAllScheduledAuctions() {
        // AuctionStatus.SCHEDULED 상태의 경매를 가져옴
        return auctionRepository.findByStatus(AuctionStatus.SCHEDULED);
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }


    public List<Auction> getAuctionLit() {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        return auctionRepository.findAll();
    }

    public void deleteAuctionById(Long id) {
        auctionRepository.deleteById(id);
    }


    public List<Auction> getAuctionListSorted() {
        // 최신순으로 정렬된 경매 목록을 반환
        return auctionRepository.findAll(Sort.by(Sort.Direction.ASC, "startDate"));
    }

    public List<Auction> searchAuctionsByKeywordSorted(String keyword) {
        // 검색어가 포함된 경매 목록을 최신순으로 정렬하여 반환
        return auctionRepository.findByNameContainingIgnoreCase(keyword, Sort.by(Sort.Direction.ASC, "startDate"));
    }

    public void productCreate(String name, LocalDateTime startDate, LocalDateTime endDate, Double startingPrice, Artist artist, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Auction auction = new Auction();
        auction.setName(name);
        auction.setStartDate(startDate);
        auction.setEndDate(endDate);
        auction.setStartingPrice(startingPrice);
        auction.setProduct(product); // 제품과 경매 연결

        auctionRepository.save(auction);
    }
}

