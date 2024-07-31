package com.example.ArtAuction_24.domain.home;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.product.service.AuctionProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final ArtistService artistService;

    private final AuctionProductService auctionProductService;

    @GetMapping("/search")
    public String search(@RequestParam(value = "kw", defaultValue = "") String keyword, Model model)
    {
        if (keyword.isEmpty()) {
            // 키워드가 비어 있는 경우
            model.addAttribute("keyword", ""); // 빈 문자열을 넘겨줌
            model.addAttribute("artistList", Collections.emptyList()); // 빈 리스트를 넘겨줌
        } else {
            // 키워드가 있는 경우
            List<Artist> artistList = artistService.findByKeyword(keyword);

            model.addAttribute("keyword", keyword); // 키워드를 넘겨줌
            model.addAttribute("artistList", artistList);

        }
        return "home/search_results";
    }

}
