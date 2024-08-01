package com.example.ArtAuction_24.domain.home;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.product.entity.AuctionProduct;
import com.example.ArtAuction_24.domain.product.service.AuctionProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AuctionProductService auctionProductService;

    @GetMapping("/")
    public String index(Model model) {
        List<AuctionProduct> auctionProductList = auctionProductService.findAllAuctionProductOrderByCreateDateDesc();

        model.addAttribute("auctionProductList", auctionProductList);

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
