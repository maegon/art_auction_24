package com.example.ArtAuction_24.domain.question.entity;

import com.example.ArtAuction_24.domain.answer.entity.Answer;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.util.List;


@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseEntity {


    private String subject;


    private String content;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne
    private Member member;

}
