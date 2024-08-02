package com.example.ArtAuction_24.domain.home;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.service.AuctionProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AuctionProductService auctionProductService;
    private final MemberService memberService;

    @GetMapping("/")
    public String index(Model model) {
        List<AuctionProduct> auctionProductList = auctionProductService.findAllAuctionProductOrderByCreateDateDesc();
        model.addAttribute("auctionProductList", auctionProductList);

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
        }

        return "home/main";
    }


    @GetMapping("/home/introduce")
    public String introduce() {
        return "home/introduce";
    }

    @GetMapping("/home/howToBuy")
    public String howToBuy(){
        return  "home/howToBuy";
    }

    @GetMapping("/home/howToSell")
    public String howToSell(){
        return  "home/howToSell";
    }
}
