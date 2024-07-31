package com.example.ArtAuction_24.domain.home;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String index() {
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
