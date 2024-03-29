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
import java.util.ArrayList;
import java.util.List;

/** 댓글 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId; // 기본키

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user; // 사용자 외래키 (다대일)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")

    private Post post; // 글 외래키 (다대일)

    private String comment; // 댓글 내용

    @OneToMany(mappedBy = "comment")
    private List<Reply> replys = new ArrayList<>(); // 대댓글 외래키 (일대다)

    /** 댓글 내용 수정 */
    @Transactional
    public void update(SaveCommentDto dto) {
        this.comment = dto.getComment();
    }

}
