package com.example.ArtAuction_24.member.entity;

import com.example.ArtAuction_24.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.File;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String address;
    
    @Column(unique = false)
    private String image; //이미지 파일 이름을 저장
    
    private String isActive;

}
