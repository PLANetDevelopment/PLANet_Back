package com.planet.develop.Entity.StarTalk;

import com.planet.develop.DTO.PostDto.SaveCommentDto;
import com.planet.develop.DTO.PostDto.SavePostDto;
import com.planet.develop.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

/** 댓글 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long commentId; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 사용자 외래키 (다대일)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 글 외래키 (다대일)

    private String comment; // 댓글 내용

    public SaveCommentDto entityToSaveDto() {
        SaveCommentDto dto = SaveCommentDto.builder()
                .postId(this.post.getPostId())
                .userId(this.user.getUserId())
                .comment(this.comment)
                .build();
        return dto;
    }

    /** 댓글 내용 수정 */
    @Transactional
    public void update(SaveCommentDto dto) {
        this.comment = dto.getComment();
    }

}
