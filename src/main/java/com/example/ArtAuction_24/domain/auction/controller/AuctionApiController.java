package com.example.ArtAuction_24.domain.auction.controller;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuctionApiController {

    private final AuctionService auctionService;


    @GetMapping("/calendar")
    public List<Map<String, Object>> getCalendarEvents() {
        List<Auction> auctions = auctionService.getAllScheduledAuctions();

        // 색상 배열 정의
        String[] colors = {"#34495E", "#E67E22", "#1ABC9C", "#9B59B6"};

        return IntStream.range(0, auctions.size())
                .mapToObj(index -> {
                    Auction auction = auctions.get(index);
                    Map<String, Object> event = new HashMap<>();
                    event.put("title", auction.getName());
                    event.put("start", auction.getStartDate().toString());
                    event.put("end", auction.getEndDate().toString());
                    event.put("url", "/auction/scheduledDetail/" + auction.getId());

                    // 색상 설정
                    String color = colors[index % colors.length]; // 색상 배열에서 색상 선택
                    event.put("backgroundColor", color);
                    event.put("borderColor", color);

                    return event;
                })
                .collect(Collectors.toList());
    }

}
