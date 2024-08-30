package com.example.ArtAuction_24.domain.post.service;

import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.post.entity.Post;
import com.example.ArtAuction_24.domain.post.form.PostForm;
import com.example.ArtAuction_24.domain.post.repository.PostRepository;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.form.QuestionForm;
import com.example.ArtAuction_24.global.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public void delete(Post post) {
        this.postRepository.delete(post);
    }



    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post getPost(Long id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public boolean existsByMember(Member m1) {
        return postRepository.existsByMember(m1);
    }
}
