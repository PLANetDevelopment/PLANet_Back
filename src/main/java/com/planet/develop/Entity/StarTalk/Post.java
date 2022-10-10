package com.planet.develop.Entity.StarTalk;

import com.planet.develop.DTO.PostDto.SavePostDto;
import com.planet.develop.Entity.BaseEntity;
import com.planet.develop.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
/** 게시글 */
public class Post extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long postId; // 기본키

    @ManyToOne(fetch = FetchType.EAGER) // LAZY로 하면 접근 권한 없다고 오류남
    @JoinColumn(name = "user_id")
    private User user; // 사용자 외래키 (다대일)
    
    private String title; // 글 제목
    
    private String content; // 글 내용

    private String category; // 카테고리

    private int likeCount; // 좋아요 수

    private int commentCount; // 댓글 수

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImageList = new ArrayList<>(); // 이미지 외래키 (일대다 양방향)

    public void increaselikeCount() {
        this.likeCount = likeCount + 1;
    }

    public void decreaselikeCount() {
        this.likeCount = likeCount - 1;
    }
    
    public void increaseCommentCount() {
        this.commentCount = commentCount + 1;
    }

    public void decreaseCommentCount() {
        this.commentCount = commentCount - 1;
    }

    /** 게시글 수정 */
    @Transactional
    public void update(SavePostDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.category = dto.getCategory();
    }

    public SavePostDto entityToSaveDto() {
        SavePostDto dto = SavePostDto.builder()
                .title(this.title)
                .content(this.content)
                .category(this.category)
                // 이미지 코드 추가
                .build();
        return dto;
    }

}