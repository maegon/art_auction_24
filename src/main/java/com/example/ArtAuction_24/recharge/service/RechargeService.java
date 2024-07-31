package com.example.ArtAuction_24.recharge.service;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.recharge.entity.Recharge;
import com.example.ArtAuction_24.recharge.repository.RechargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RechargeService {

    private final RechargeRepository rechargeRepository;
    private final MemberRepository memberRepository;

    public Recharge recharge(Long memberId, Long amount) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Recharge recharge = new Recharge();
        recharge.setMember(member);
        recharge.setAmount(amount);
        recharge.setRechargeDate(LocalDateTime.now());

        // 멤버의 잔액 업데이트 로직 추가
        member.setBalance(member.getBalance() + amount);
        memberRepository.save(member);

        return rechargeRepository.save(recharge);
    }

    public List<Recharge> getRechargeHistory(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return rechargeRepository.findByMember(member);
    }
}
