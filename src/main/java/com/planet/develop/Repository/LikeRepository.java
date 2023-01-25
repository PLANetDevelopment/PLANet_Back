package com.planet.develop.Repository;

import com.planet.develop.Entity.StarTalk.LikeTbl;
import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeTbl, Long> {
    LikeTbl findByLikeId(Long likeId); // 기본키로 찾기
    Long countAllByPost(Post post); // 좋아요 수 찾기
    Long countAllByPost(Long postId); // 좋아요 수 찾기
    LikeTbl findByUserAndPost(User user, Post post); // 사용자와 게시글로 좋아요 아이디 찾기
}
