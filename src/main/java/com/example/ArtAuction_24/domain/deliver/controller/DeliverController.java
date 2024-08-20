package com.example.ArtAuction_24.domain.deliver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DeliverController {

    @GetMapping("/deliver/status")
    public String viewDeliveryStatus() {
        // deliverStatus.html 파일을 반환
        return "deliver/deliver";
    }
}
