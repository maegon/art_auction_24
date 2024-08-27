package com.example.ArtAuction_24.domain.question.repository;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByAnsweredTrue(Pageable pageable);
    Page<Question> findByAnsweredFalse(Pageable pageable);

    List<Question> findByMember(Member member);
}

