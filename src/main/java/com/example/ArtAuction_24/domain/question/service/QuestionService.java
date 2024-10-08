package com.example.ArtAuction_24.domain.question.service;


import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.product.entity.LikeProduct;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.entity.QuestionType;
import com.example.ArtAuction_24.domain.question.form.QuestionForm;
import com.example.ArtAuction_24.domain.question.repository.QuestionRepository;
import com.example.ArtAuction_24.global.DataNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

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




    public Question create(QuestionForm questionForm, Member member) {
        Question q = new Question();
        q.setSubject(questionForm.getSubject());
        q.setContent(questionForm.getContent());
        q.setQuestionType(questionForm.getQuestionType());
        q.setCreateDate(LocalDateTime.now());
        q.setMember(member);
        return this.questionRepository.save(q);
    }



    public void create(QuestionForm questionForm,MultipartFile thumbnail, Member member ) {

        String thumbnailRelPath = "question/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

        if (!thumbnailFile.getParentFile().exists()) {
            thumbnailFile.getParentFile().mkdirs();
        }
        try {
            thumbnail.transferTo(thumbnailFile);
        } catch( IOException e) {
            throw new RuntimeException(e);
        }

        Question q = new Question();
        q.setSubject(questionForm.getSubject());
        q.setContent(questionForm.getContent());
        q.setQuestionType(questionForm.getQuestionType());
        q.setCreateDate(LocalDateTime.now());
        q.setThumbnailImg(thumbnailRelPath);
        q.setMember(member);
        this.questionRepository.save(q);
    }


    public Page<Question> getAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    public Page<Question> findAll(Pageable pageable) {
        // 처리 완료 여부를 기준으로 정렬 조건을 설정
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.asc("answered"), Sort.Order.desc("id")));
        return questionRepository.findAll(sortedPageable);
    }


    @Transactional
    public void markQuestionAsAnswered(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        question.setAnswered(true);
        questionRepository.save(question);
    }

    public Page<Question> findByAnsweredTrue(Pageable pageable){
        return questionRepository.findByAnsweredTrue(pageable);
    }
    public Page<Question> findByAnsweredFalse(Pageable pageable){
        return questionRepository.findByAnsweredFalse(pageable);
    }


    public List<Question> findByMember(Member member) {
        return questionRepository.findByMember(member);
    }

    public boolean existsByMember(Member m1) {
        return questionRepository.existsByMember(m1);
    }
}
