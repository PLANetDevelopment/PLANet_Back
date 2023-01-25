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

/** 대댓글 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 사용자 외래키 (다대일)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment; // 댓글 외래키 (다대일)

    private String reply; // 댓글 내용

    /** 댓글 내용 수정 */
    @Transactional
    public void update(SaveCommentDto dto) {
        this.reply = dto.getComment();
    }

}
