package com.planet.develop.Entity.StarTalk;

import com.planet.develop.Entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPostDto {
    private Long postId; // 기본키
    private User user; // 사용자
    private String title; // 글 제목
    private String content; // 글 내용
    private String category; // 카테고리
    private int likeCount; // 좋아요 수
    private int commentCount; // 댓글 수
    private List<PostImage> postImageList = new ArrayList<>(); // 이미지
}
