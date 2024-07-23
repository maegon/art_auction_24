package com.example.ArtAuction_24.question.entity;

import com.example.ArtAuction_24.answer.entity.Answer;
import com.example.ArtAuction_24.base.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
}
