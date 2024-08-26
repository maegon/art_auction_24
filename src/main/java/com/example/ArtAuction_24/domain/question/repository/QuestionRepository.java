package com.example.ArtAuction_24.domain.question.repository;

import com.example.ArtAuction_24.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByAnsweredTrue(Pageable pageable);
    Page<Question> findByAnsweredFalse(Pageable pageable);
}

