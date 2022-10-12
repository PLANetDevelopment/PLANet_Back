package com.planet.develop.Repository;

import com.planet.develop.Entity.StarTalk.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByLikeId(Long likeId); // 기본키로 찾기
}
