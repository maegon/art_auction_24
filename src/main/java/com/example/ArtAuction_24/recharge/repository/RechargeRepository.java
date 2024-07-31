package com.example.ArtAuction_24.recharge.repository;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.recharge.entity.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {
    List<Recharge> findByMember(Member member);
}
