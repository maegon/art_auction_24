package com.example.ArtAuction_24.answer.entity;

import com.example.ArtAuction_24.base.entity.BaseEntity;

import com.example.ArtAuction_24.question.entity.Question;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.Column;
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


}
