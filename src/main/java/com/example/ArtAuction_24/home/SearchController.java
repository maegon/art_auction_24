package com.example.ArtAuction_24.home;

import com.example.ArtAuction_24.artist.service.ArtistService;
import com.example.ArtAuction_24.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final ArtistService artistService;

    private final ProductService productService;

    @GetMapping("/search")
    public String search(@RequestParam(value = "kw", defaultValue = "") String keyword, Model model)
    {


        return "home/search_results";
    }

}
