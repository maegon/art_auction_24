package com.example.ArtAuction_24.domain.question.service;


import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.entity.QuestionType;
import com.example.ArtAuction_24.domain.question.form.QuestionForm;
import com.example.ArtAuction_24.domain.question.repository.QuestionRepository;
import com.example.ArtAuction_24.global.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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

        String thumbnailRelPath = "image/question/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

        thumbnailFile.mkdir();

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


}
