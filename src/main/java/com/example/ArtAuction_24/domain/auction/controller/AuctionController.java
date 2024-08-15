package com.example.ArtAuction_24.domain.auction.controller;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.auction.form.AuctionForm;
import com.example.ArtAuction_24.domain.auction.service.AuctionService;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {
    private final AuctionService auctionService;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ArtistService artistService;

    @GetMapping("/list")
    public String list(Pageable pageable, Model model,
                       @RequestParam(value = "kw", required = false) String keyword,
                       @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
                       @RequestParam(value = "auctionName", required = false) String auctionName) {

        // 필터링 및 정렬된 제품 목록 가져오기
        Page<Product> paging = auctionService.getProductsWithFilteringAndSorting(keyword, auctionName, pageable, sort);

        model.addAttribute("categories", auctionService.getDistinctAuctionNames());
        model.addAttribute("paging", paging);
        model.addAttribute("kw", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("auctionName", auctionName);

        return "auction/list";
    }

    @GetMapping("/scheduled")
    public String scheduledAuctions(Model model) {
        List<Auction> scheduledAuctions = auctionService.getScheduledAuctions();
        model.addAttribute("scheduledAuctions", scheduledAuctions);
        return "auction/scheduled";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/create")
    public String create(AuctionForm auctionForm, Model model) {
        // Filter products where winningBidder is null
        List<Product> availableProducts = productRepository.findByWinningBidderIsNull();
        model.addAttribute("allProducts", availableProducts);
        return "auction/form";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public String auctionCreate(@Valid AuctionForm auctionForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "auction/form";
        }

        // Fetch products based on IDs
        List<Product> products = productRepository.findAllById(auctionForm.getProducts());

        if (products.size() != auctionForm.getProducts().size()) {
            // Handle case where some products could not be found
            bindingResult.rejectValue("products", "error.products", "One or more products are not available.");
            return "auction/form";
        }

        auctionService.create(auctionForm.getName(), auctionForm.getStartDate(), auctionForm.getEndDate(), auctionForm.getProducts());
        return "redirect:/auction/list";
    }
}
