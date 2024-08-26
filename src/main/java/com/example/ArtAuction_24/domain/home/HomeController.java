package com.example.ArtAuction_24.domain.home;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.entity.AuctionStatus;
import com.example.ArtAuction_24.domain.auction.service.AuctionService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final MemberService memberService;
    private final AuctionService auctionService;

    @GetMapping("/")
    public String index(Model model, HttpServletResponse response) {
        List<Product> productList = productService.findAllProductOrderByCreateDateDesc();
        model.addAttribute("productList", productList);

        Product topProduct = productService.getTopProductByView();
        model.addAttribute("topProduct", topProduct);

        // 현재 로그인한 사용자의 프로필 이미지와 이름을 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String username = authentication.getName();
            Optional<Member> currentMemberOpt = memberService.findByUsername(username);
            if (currentMemberOpt.isPresent()) {
                Member currentMember = currentMemberOpt.get();
                model.addAttribute("profileImage", currentMember.getImage());
                model.addAttribute("username", currentMember.getUsername());
            }

            // 재원 추가
            // 현재 인증된 사용자의 권한을 모델에 추가
            String authority = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("UNKNOWN");

            model.addAttribute("userAuthority", authority);
        }

        // ACTIVE 상태의 경매에 포함된 제품을 가져옵니다.
        List<Product> activeAuctionProducts = productService.findProductsByAuctionStatus(AuctionStatus.ACTIVE);
        model.addAttribute("activeAuctionProducts", activeAuctionProducts);

        // SCHEDULED 상태의 경매의 정보를 가져옵니다.
        List<Auction> scheduledAuctions = auctionService.getScheduledAuctions();
        List<Auction> limitedScheduledAuctions = scheduledAuctions.stream()
                .limit(2)
                .collect(Collectors.toList());
        model.addAttribute("scheduledAuctions", limitedScheduledAuctions);

        // 재원 추가
        // 캐시 정보를 비활성화합니다.
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.

        return "home/main";
    }


    @GetMapping("/home/introduce")
    public String introduce() {
        return "home/introduce";
    }

    @GetMapping("/home/howToBuy")
    public String howToBuy(){
        return "home/howToBuy";
    }

    @GetMapping("/home/howToSell")
    public String howToSell(){
        return "home/howToSell";
    }

    @GetMapping("/home/paint")
    public String paintPage(){
        return "home/paint";
    }
}
