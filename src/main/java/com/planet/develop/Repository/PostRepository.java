package com.planet.develop.Repository;

import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByPostId(Long postId); // 기본키로 찾기
    List<Post> findAllByUser(User user); // 사용자로 찾기
    List<Post> findAllByCategory(String category); // 카테고리로 찾기
    void deleteByPostId(Long postId); // 기본키로 삭제하기

}
