package com.example.ArtAuction_24.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarPageController {

    @GetMapping("/view")
    public String calendarPage() {
        return "calendar/view";
    }
}
