package com.example.ArtAuction_24.domain.member.repository;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.recharge.entity.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByUsernameAndProviderTypeCode(String username, String providerTypeCode);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    Optional<Member> findByUsernameAndEmail(String username, String email);

    Optional<Object> findByEmail(String email);

}
