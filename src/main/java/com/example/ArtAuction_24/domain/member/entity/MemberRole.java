package com.example.ArtAuction_24.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("ADMIN"),
    ARTIST("ARTIST"),
    MEMBER("MEMBER");

    private String value;

    MemberRole(String value) {
        this.value = value;
    }
}
