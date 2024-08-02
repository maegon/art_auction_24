package com.example.ArtAuction_24.domain.question.service;


import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.entity.QuestionType;
import com.example.ArtAuction_24.domain.question.form.QuestionForm;
import com.example.ArtAuction_24.domain.question.repository.QuestionRepository;
import com.example.ArtAuction_24.global.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }


    public void create(QuestionForm questionForm, QuestionType questionType) {
        Question q = new Question();
        q.setSubject(questionForm.getSubject());
        q.setContent(questionForm.getContent());
        q.setQuestionType(questionType);
        q.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q);
    }


}
