package com.planet.develop.DTO.PostDto;

import com.planet.develop.Entity.StarTalk.Post;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private List<Post> postList;
}
