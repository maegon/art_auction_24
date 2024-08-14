package com.example.ArtAuction_24.domain.bid.controller;

import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.bid.service.BidService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auction")
public class BidController {

    private final BidService bidService;
    private final MemberService memberService;
    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(BidController.class);

    @PostMapping("/bid")
    public String placeBid(
            @RequestParam(name = "productId") Long productId,
            @RequestParam(name = "bidAmount") BigDecimal bidAmount,
            Principal principal) {

        // 현재 사용자의 정보를 가져옴
        Optional<Member> optionalMember = memberService.findByUsername(principal.getName());
        if (optionalMember.isEmpty()) {
            return "redirect:/product/detail/" + productId + "?error=" + URLEncoder.encode("회원을 찾을 수 없습니다", StandardCharsets.UTF_8);
        }

        Member member = optionalMember.get();  // Optional에서 Member 객체를 추출

        Product product = productService.findById(productId);

        BigDecimal memberBalance = BigDecimal.valueOf(member.getBalance());
        // 사용자의 잔액이 입찰 금액보다 부족할 경우
        if (memberBalance.compareTo(bidAmount) < 0) {
            return "redirect:/product/detail/" + productId + "?error=" + URLEncoder.encode("잔액이 부족합니다.", StandardCharsets.UTF_8);
        }

        // 현재가와 사용자 잔액 확인

        if (product.getCurrentBid().compareTo(bidAmount) >= 0) {
            return "redirect:/product/detail/" + productId + "?error=" + URLEncoder.encode("현재가보다 높은 금액을 입력해주세요", StandardCharsets.UTF_8);
        }



        // 입찰 정보 저장
        bidService.placeBid(productId, member.getId(), bidAmount);

        return "redirect:/product/detail/" + productId;
    }

    @PostMapping("/cancelBid")
    public String cancelBid(@RequestParam(name = "bidId") Long bidId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // 현재 사용자의 정보를 가져옴
            Optional<Member> optionalMember = memberService.findByUsername(principal.getName());
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                // 현재 사용자의 입찰을 취소할 수 있도록 확인
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
