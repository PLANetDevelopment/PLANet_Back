package com.planet.develop;

import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestController {

    private final PostService postService;

    public TestController(PostService postService) {
        this.postService = postService;
    }

    @Test
    /** 전체 게시글 가져오기 */
    public List<Post> findAll(){
        return postService.findAll();
    }

}
