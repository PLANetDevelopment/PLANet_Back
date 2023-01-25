package com.planet.develop.DTO.PostDto;

import com.planet.develop.Entity.StarTalk.Comment;
import com.planet.develop.Entity.StarTalk.Post;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
/** 게시글 & 댓글 */
public class PostAndCommentDto {
    private Post post; // 게시글
    private Long likeCount; // 좋아요 수
    private Long commentCount; // 댓글 수
    private List<Comment> comment; // 댓글 내용
}
