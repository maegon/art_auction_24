package com.example.ArtAuction_24.domain.member.dto;

import com.example.ArtAuction_24.domain.member.entity.MemberRole;
import lombok.Data;

@Data
public class AdmUpdateMemberRequest {
    private MemberRole role;
    private Boolean isActive;
}
