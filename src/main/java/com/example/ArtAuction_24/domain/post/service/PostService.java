package com.example.ArtAuction_24.domain.post.service;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.post.entity.Post;
import com.example.ArtAuction_24.domain.post.form.PostForm;
import com.example.ArtAuction_24.domain.post.repository.PostRepository;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.form.QuestionForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post create(PostForm postForm, Member member) {
        Post p = new Post();
        p.setSubject(postForm.getSubject());
        p.setContent(postForm.getContent());
        p.setPostType(postForm.getPostType());
        p.setCreateDate(LocalDateTime.now());
        p.setMember(member);
        return this.postRepository.save(p);
    }
}
