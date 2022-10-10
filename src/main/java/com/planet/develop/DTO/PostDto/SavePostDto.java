package com.planet.develop.DTO.PostDto;

import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Repository.UserRepository;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
/** 게시글 저장 */
public class SavePostDto {
    private String userId; // 사용자 아이디
    private String title; // 제목
    private String content; // 내용
    private String category; // 카테고리
    private List<String> images; // 이미지

    private UserRepository userRepository;

    public Post dtoToEntity() {
        Post post = Post.builder()
                .title(this.title)
                .content(this.content)
                .category(this.category)
                .user(userRepository.findById(userId).get())
                .likeCount(0)
                .commentCount(0)
                // 이미지 코드 추가
                .build();
        return post;
    }
}
