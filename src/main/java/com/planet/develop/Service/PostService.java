package com.planet.develop.Service;

import com.planet.develop.DTO.PostDto.SavePostDto;
import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Entity.User;

import java.util.List;

public interface PostService {

    void save(SavePostDto post); // 게시글 저장
    void update(Long postId, SavePostDto post); // 게시글 수정
    void delete(Long postId); // 게시글 삭제
    List<Post> findAll(); // 전체 게시글 가져오기
    List<Post> findAllByUser(User user); // 사용자별 게시글 가져오기
    List<Post> findAllByCategory(String category); // 카테고리별 게시글 가져오기
    List<Post> sortByLike(); // 좋아요 순으로 정렬하기
    List<Post> sortByOldest(); // 오래된 순으로 정렬하기
    List<Post> sortByNewest(); // 최근 순으로 정렬하기


}
