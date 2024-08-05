package com.example.ArtAuction_24.domain.product.controller;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.service.AuctionProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auctionProduct")
public class AuctionProductController {

    private final AuctionProductService auctionProductService;

    @GetMapping("/list")
    public String list(Pageable pageable, Model model,
                       @RequestParam(value = "kw", required = false) String keyword,
                       @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort) {
        Page<AuctionProduct> paging = auctionProductService.getProductsWithSorting(keyword, pageable, sort);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", keyword);
        model.addAttribute("sort", sort);
        return "auctionProduct/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        auctionProductService.incrementViews(id);
        AuctionProduct auctionProduct = auctionProductService.getProduct(id);
        model.addAttribute("auctionProduct", auctionProduct);
        return "auctionProduct/detail";
    }
}
