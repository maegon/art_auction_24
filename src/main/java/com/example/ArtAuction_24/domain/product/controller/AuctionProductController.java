package com.example.ArtAuction_24.domain.product.controller;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.service.AuctionProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/AuctionProduct")
public class AuctionProductController {

    private final AuctionProductService auctionProductService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw){

        Page<AuctionProduct> paging = auctionProductService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "AuctionProduct/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        AuctionProduct auctionProduct = auctionProductService.getProduct(id);

        model.addAttribute("auctionProduct", auctionProduct);
        System.out.println(auctionProduct.toString());
        return "AuctionProduct/detail";
    }


}
