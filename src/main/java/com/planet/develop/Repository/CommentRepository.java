package com.planet.develop.Repository;

import com.planet.develop.Entity.StarTalk.Comment;
import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByCommentId(Long commentId); // 기본키로 찾기
    List<Comment> findByPost(Post post); // 게시글로 찾기
    Long countAllByPost(Post post); // 댓글 수 찾기
    Long countAllByPost(Long postId); // 댓글 수 찾기
    List<Comment> findAllByUser(User user); // 내 댓글 찾기
}
