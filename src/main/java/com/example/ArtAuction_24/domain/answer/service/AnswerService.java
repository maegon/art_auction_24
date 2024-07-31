package com.example.ArtAuction_24.domain.answer.service;

import com.example.ArtAuction_24.domain.answer.entity.Answer;
import com.example.ArtAuction_24.domain.answer.repository.AnswerRepository;
import com.example.ArtAuction_24.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;


    public void create(Question question, String content) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        this.answerRepository.save(answer);
    }
}
