package com.planet.develop.Repository;

import com.planet.develop.Entity.StarTalk.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByCommentId(Long commentId); // 기본키로 찾기
    List<Comment> findByPostId(Long postId); // 게시글 아이디로 찾기
}
