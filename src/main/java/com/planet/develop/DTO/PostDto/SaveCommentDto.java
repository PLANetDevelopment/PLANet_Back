package com.planet.develop.DTO.PostDto;

import com.planet.develop.Entity.StarTalk.Comment;
import com.planet.develop.Repository.PostRepository;
import com.planet.develop.Repository.UserRepository;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
/** 댓글 */
public class SaveCommentDto {
    private Long commentId; // 아이디
    private Long postId; // 게시글 아이디
    private String userId; // 사용자 아이디
    private String comment; // 내용

    private UserRepository userRepository;
    private PostRepository postRepository;

    public Comment dtoToEntity() {
        Comment comment = Comment.builder()
                .commentId(this.commentId)
                .post(postRepository.findByPostId(this.postId))
                .comment(this.comment)
                .user(userRepository.findById(this.userId).get())
                .build();
        return comment;
    }
}