package com.example.ArtAuction_24.recharge.controller;

import com.example.ArtAuction_24.recharge.entity.Recharge;
import com.example.ArtAuction_24.recharge.service.RechargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recharge")
public class RechargeController {
 private final RechargeService rechargeService;

    @PostMapping("/{memberId}")
    public ResponseEntity<Recharge> recharge(
            @PathVariable Long memberId,
            @RequestParam Long amount) {

        Recharge recharge = rechargeService.recharge(memberId, amount);
        return ResponseEntity.ok(recharge);
    }

    @GetMapping("/history/{memberId}")
    public ResponseEntity<List<Recharge>> getRechargeHistory(@PathVariable Long memberId) {
        List<Recharge> history = rechargeService.getRechargeHistory(memberId);
        return ResponseEntity.ok(history);
    }
}
