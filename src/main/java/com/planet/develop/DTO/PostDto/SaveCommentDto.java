package com.planet.develop.DTO.PostDto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
/** 댓글 */
public class SaveCommentDto {
    private Long postId; // 게시글 아이디
    private String userId; // 사용자 아이디
    private String comment; // 내용
}