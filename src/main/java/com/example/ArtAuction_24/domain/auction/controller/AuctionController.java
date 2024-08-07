package com.example.ArtAuction_24.domain.auction.controller;

import com.example.ArtAuction_24.domain.auction.form.AuctionForm;
import com.example.ArtAuction_24.domain.auction.service.AuctionService;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {
    private final AuctionService auctionService;
    private final ProductRepository productRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(AuctionForm auctionForm, Model model) {
        List<Product> allProducts = productRepository.findAll();
        model.addAttribute("allProducts", allProducts);

        return "auction/form";
    }

    @PreAuthorize("isAuthenticated()")
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
        return "redirect:/product/list";
    }
}
