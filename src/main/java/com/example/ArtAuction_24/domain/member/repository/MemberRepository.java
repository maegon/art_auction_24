package com.example.ArtAuction_24.domain.member.repository;

import com.example.ArtAuction_24.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Page<Member> findAll(Pageable pageable);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.username LIKE %:keyword% OR m.email LIKE %:keyword%")
    Page<Member> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    List<Member> findByProofSubmittedTrueAndApprovedArtistFalse();

    // 승인된 멤버들을 가져오는 메서드
    List<Member> findByProofSubmittedTrueAndApprovedArtistTrue();
}
