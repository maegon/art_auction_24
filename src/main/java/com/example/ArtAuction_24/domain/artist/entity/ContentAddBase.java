package com.example.ArtAuction_24.domain.artist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@SuperBuilder
@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class ContentAddBase {

    private String content;

    public abstract String getContent();

}