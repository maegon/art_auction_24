package com.example.ArtAuction_24.domain.product.controller;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.service.AuctionProductService;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    private final MemberService memberService;


    @GetMapping("/list")
    public String list(Pageable pageable, Model model,
                       @RequestParam(value = "kw", required = false) String keyword,
                       @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
                       @RequestParam(value = "auction", required = false) Boolean auction) {
        Page<Product> paging = productService.getProductsWithSorting(keyword, pageable, sort, auction);

        //로그인한 정보 에서 member 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Member member = memberService.getMember(userDetails.getUsername());
            model.addAttribute("member", member);

            // 찜 상태 조회
            Set<Long> likedProductIds = productService.getLikedProductIdsByMember(member.getId());
            model.addAttribute("likedProductIds", likedProductIds);

        }



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

    @PostMapping("/like")
    public String likeProduct(@RequestParam("productId") Long productId,
                              @RequestParam("memberId") Long memberId,
                              HttpServletRequest request) {
        productService.toggleLike(productId, memberId);
        // 현재 페이지의 URL을 가져와서 리다이렉트
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;

    }


}
