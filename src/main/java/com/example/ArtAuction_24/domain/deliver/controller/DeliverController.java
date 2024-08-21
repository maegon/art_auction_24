package com.example.ArtAuction_24.domain.deliver.controller;

import com.example.ArtAuction_24.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/deliver")
public class DeliverController {

    private final OrderService orderService;

    @GetMapping("/status")
    public String viewDeliveryStatus() {

        return "deliver/deliver";
    }
}
