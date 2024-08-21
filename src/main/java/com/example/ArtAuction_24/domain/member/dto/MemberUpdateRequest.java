package com.example.ArtAuction_24.domain.member.dto;

import com.example.ArtAuction_24.domain.member.entity.MemberRole;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class MemberUpdateRequest {
    private String role;
    private Boolean isActive;

    // 매개변수가 있는 생성자
    public MemberUpdateRequest(boolean isActive, String role) {
        this.isActive = isActive;
        this.role = role;
    }
}
