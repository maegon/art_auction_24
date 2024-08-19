package com.example.ArtAuction_24.domain.post.repository;

import com.example.ArtAuction_24.domain.post.entity.Post;
import com.example.ArtAuction_24.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
