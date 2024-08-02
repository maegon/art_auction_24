package com.example.ArtAuction_24.domain.answer.entity;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.global.base.entity.BaseEntity;

import com.example.ArtAuction_24.domain.question.entity.Question;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends BaseEntity {


    private String content;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Member member;


}
