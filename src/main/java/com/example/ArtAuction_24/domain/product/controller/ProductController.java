package com.example.ArtAuction_24.domain.product.controller;


import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.bid.service.BidService;
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

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final MemberService memberService;
    private final BidService bidService;


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
    public String detail(@PathVariable("id") Long id, Model model) {
        productService.incrementViews(id);
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);

        String auctionStatus = "SCHEDULED"; // 기본값을 "SCHEDULED"로 설정

        // 특정 제품의 가장 최신 경매 상품 조회
        Optional<AuctionProduct> latestAuctionProduct = productService.getLatestAuctionProduct(id);

        if (latestAuctionProduct.isPresent()) {
            AuctionProduct auctionProduct = latestAuctionProduct.get();
            Auction auction = auctionProduct.getAuction();

            if (auction != null) {
                auctionStatus = auction.getStatus().name();
            }

            model.addAttribute("auctionProduct", auctionProduct);
        } else {
            model.addAttribute("auctionProduct", null);
        }

        model.addAttribute("auctionStatus", auctionStatus);

        // 현재 사용자의 입찰 정보 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = null;

            if (authentication.getPrincipal() instanceof UserDetails) {
                username = ((UserDetails) authentication.getPrincipal()).getUsername();
            } else {
                username = authentication.getName();
            }

            if (username != null) {
                Optional<Member> optionalMember = memberService.findByUsername(username);
                if (optionalMember.isPresent()) {
                    Member member = optionalMember.get();
                    Optional<Bid> currentBid = bidService.findCurrentBidByProductAndMember(id, member.getId());
                    model.addAttribute("currentBid", currentBid.orElse(null));
                }
            }
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

    @PostMapping("/cancelBid")
    public String cancelBid(@RequestParam(name = "bidId") Long bidId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // 현재 사용자의 입찰만 취소할 수 있도록 확인
            Optional<Member> optionalMember = memberService.findByUsername(principal.getName());
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                bidService.cancelBid(bidId, member.getId());
                redirectAttributes.addFlashAttribute("message", "입찰이 성공적으로 취소되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "입찰 취소에 실패했습니다.");
        }
        return "redirect:/product/detail/" + bidId; // 적절히 리다이렉트 URL을 수정
    }



}
