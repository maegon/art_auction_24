package com.example.ArtAuction_24.question.service;

import com.example.ArtAuction_24.question.entity.Question;
import com.example.ArtAuction_24.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    public List<Question> findAll() {
        return questionRepository.findAll();
    }
}
