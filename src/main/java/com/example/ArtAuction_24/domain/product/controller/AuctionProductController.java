package com.example.ArtAuction_24.domain.product.controller;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.service.AuctionProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auctionProduct")
public class AuctionProductController {

    private final AuctionProductService auctionProductService;

    @GetMapping("/{id}")
    public String getAuctionProduct(@PathVariable Long id, Model model) {
        AuctionProduct auctionProduct = auctionProductService.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("auctionProduct", auctionProduct);
        return "auctionProduct/detail";
    }

    @GetMapping("/list")
    public String listAuctionProducts(@RequestParam Long auctionId, Model model) {
        List<AuctionProduct> auctionProducts = auctionProductService.findByAuctionId(auctionId);
        model.addAttribute("auctionProducts", auctionProducts);
        return "auctionProduct/list";
    }

    @PostMapping("/create")
    public String createAuctionProduct(@ModelAttribute AuctionProduct auctionProduct) {
        auctionProductService.createAuctionProduct(auctionProduct);
        return "redirect:/auctionProduct/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteAuctionProduct(@PathVariable Long id) {
        auctionProductService.deleteById(id);
        return "redirect:/auctionProduct/list";
    }
}
