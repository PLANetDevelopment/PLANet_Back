package com.planet.develop.Service;

import com.planet.develop.DTO.PostDto.PostAndCommentDto;
import com.planet.develop.DTO.PostDto.SaveCommentDto;
import com.planet.develop.DTO.PostDto.SavePostDto;
import com.planet.develop.Entity.StarTalk.Comment;
import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Entity.StarTalk.Reply;
import com.planet.develop.Entity.StarTalk.Scrap;
import com.planet.develop.Entity.User;

import java.util.List;

public interface PostService {

    void save(SavePostDto post); // 게시글 저장
    void update(Long postId, SavePostDto post); // 게시글 수정
    void delete(Long postId); // 게시글 삭제
    List<Post> findAll(); // 전체 게시글 가져오기
    List<PostAndCommentDto> findAllPostAndComment(); // 게시글 & 댓글 가져오기
    List<Post> findAllByUser(User user); // 사용자별 게시글 가져오기

    List<Scrap> findAllByScrap(User user); // 스크랩한 게시글 가져오기
    void scrap(Long postId, String userId); // 스크랩 하기
    void canceScrap(Long postId, String userId); // 스크랩 하기

    List<Comment> findMyComment(String userId); // 내가 쓴 댓글
    List<Reply> findMyReply(String userId); // 내가 쓴 대댓글

    List<Post> findAllByCategory(String category); // 카테고리별 게시글 가져오기
    List<Post> sortByLike(); // 좋아요 순으로 정렬하기
    List<Post> sortByOldest(); // 오래된 순으로 정렬하기
    List<Post> sortByNewest(); // 최근 순으로 정렬하기

    void like(Long postId, String userId); // 좋아요
    void cancelLike(String userId, Long likeId); // 좋아요 취소

    void comment(SaveCommentDto dto); // 댓글 달기
    void updateComment(Long commentId, SaveCommentDto dto); // 댓글 수정
    void deleteComment(Long commentId); // 댓글 삭제

    /** 검색 엔진 */
    String[] splitQuery(String query); // 질의어 자르기
    List<Long> retrieveTitle(String query); // 제목 검색
    List<Long> retrieveContent(String query); // 본문 검색
    List<Long> retrieveAllTitle(String[] querys); // 제목에서 질의어 전체 검색
    List<Long> retrieveAllContent(String[] querys); // 본문에서 질의어 전체 검색
    List<Post> retrieveAll(String querys); // 검색 결과
    List<Post> postIdToPost(List<Long> postId); // List<postId> -> List<post>
}
