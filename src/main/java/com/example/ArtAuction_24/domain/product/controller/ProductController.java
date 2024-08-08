package com.example.ArtAuction_24.domain.product.controller;

import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.service.AuctionProductService;
import com.example.ArtAuction_24.domain.product.service.ProductService;
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
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;


    @GetMapping("/list")
    public String list(Pageable pageable, Model model,
                       @RequestParam(value = "kw", required = false) String keyword,
                       @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
                       @RequestParam(value = "auction", required = false) Boolean auction) {
        Page<Product> paging = productService.getProductsWithSorting(keyword, pageable, sort, auction);


        model.addAttribute("paging", paging);
        model.addAttribute("kw", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("auction", auction);
        return "product/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        productService.incrementViews(id);
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        try {
            AuctionProduct auctionProduct = productService.getAuctionProduct(id);
            model.addAttribute("auctionProduct", auctionProduct);
        } catch (RuntimeException e) {
            // Handle the case where no auction information is available
            model.addAttribute("auctionProduct", null);
        }
        return "product/detail";
    }


}
