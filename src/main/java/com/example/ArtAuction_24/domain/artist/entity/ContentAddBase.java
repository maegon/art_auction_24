package com.example.ArtAuction_24.domain.artist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class ContentAddBase {

    private String content;

    public abstract String getContent();

    public void setContent(String newContent) {
        this.content = newContent;
    }
}
