package com.example.ArtAuction_24.domain.auction.controller;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {
    private final AuctionService auctionService;
    private final ProductService productService;
    private final ProductRepository productRepository;

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

    @GetMapping("/scheduledDetail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Auction auction = auctionService.getScheduledAuctionById(id);
        if (auction == null) {
            // 경매가 존재하지 않거나 스케줄 상태가 아닐 때 처리
            return "redirect:/auction/scheduled"; // 예를 들어, 경매 목록 페이지로 리다이렉트
        }
        model.addAttribute("auction", auction);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String startDateStr = auction.getStartDate().format(formatter);
        model.addAttribute("startDate", startDateStr.toString());
        return "auction/scheduledDetail"; // 새로운 Thymeleaf 템플릿 페이지 이름
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/create")
    public String create(AuctionForm auctionForm, Model model) {
        // Filter products where winningBidder is null
        List<Product> availableProducts = auctionService.getAvailableProducts();
        model.addAttribute("allProducts", availableProducts);
        return "auction/form";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public String auctionCreate(@Valid AuctionForm auctionForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal, Model model) {
        if (!auctionForm.isStartDateValid()) {
            bindingResult.rejectValue("startDate", "error.startDate", "시작 시간은 현재 시간과 같거나 이후여야 합니다.");
        }

        if (!auctionForm.isEndDateValid()) {
            bindingResult.rejectValue("endDate", "error.endDate", "마감 시간은 시작 시간보다 이후여야 합니다.");
        }


        if (bindingResult.hasErrors()) {
            // 모든 제품을 다시 모델에 추가 (이 부분이 중요)
            List<Product> availableProducts = auctionService.getAvailableProducts();
            model.addAttribute("allProducts", availableProducts);

            return "auction/form";
        }


        List<Product> products = productRepository.findAllById(auctionForm.getProducts());

        // 찾지 못한 제품이 있으면 에러 처리
        if (products.size() != auctionForm.getProducts().size()) {
            bindingResult.rejectValue("products", "error.products", "One or more products are not available.");
            List<Product> availableProducts = auctionService.getAvailableProducts();
            model.addAttribute("allProducts", availableProducts);

            return "auction/form";
        }

        try {
            auctionService.create(auctionForm.getName(), auctionForm.getStartDate(), auctionForm.getEndDate(), auctionForm.getProducts());
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.name", e.getMessage());
            List<Product> availableProducts = auctionService.getAvailableProducts();
            model.addAttribute("allProducts", availableProducts);
            return "auction/form";
        }

        return "redirect:/auction/list";
    }

    @GetMapping("/calendar")
    public List<Auction> getScheduledAuctions() {
        return auctionService.getAllScheduledAuctions();
    }

}
