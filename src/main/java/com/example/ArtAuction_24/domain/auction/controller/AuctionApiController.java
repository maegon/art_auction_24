package com.example.ArtAuction_24.domain.auction.controller;

import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.service.AuctionService;
import com.example.ArtAuction_24.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    private final NotificationService notificationService;


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

    @PostMapping("/{auctionId}/set-notification")
    public ResponseEntity<Void> setAuctionNotification(@PathVariable("auctionId") Long auctionId, Principal principal) {
        String username = principal.getName();  // 현재 로그인한 유저의 이름
        notificationService.scheduleAuctionNotification(auctionId, username);
        return ResponseEntity.ok().build();
    }

}
