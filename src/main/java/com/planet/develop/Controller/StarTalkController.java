package com.planet.develop.Controller;

import com.planet.develop.DTO.PostDto.SavePostDto;
import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Entity.User;
import com.planet.develop.Repository.UserRepository;
import com.planet.develop.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class StarTalkController {

    private final UserRepository userRepository;
    private final PostService postService;

//    @GetMapping("/starTalk")
//    public List<Post> findAll(@RequestHeader(JwtProperties.USER_ID) String userId){
//        User user = userRepository.findById(userId).get();
//        return null;
//    }

    /** 테스트 용 ------------------------------------------------------------------ */

    /** 전체 게시글 가져오기 */
    @GetMapping("/starTalk")
    public List<Post> findAll(){
        return postService.findAll();
    }

    /** 내 게시글 가져오기 */
    @GetMapping("/starTalk/myPost")
    public List<Post> findAllByUser(){
        User user = userRepository.findById("topjoy22@naver.com").get();
        return postService.findAllByUser(user);
    }

    /** 카테고리별 게시글 가져오기 */
    @GetMapping("/starTalk/{category}")
    public List<Post> savePost(@PathVariable("category") String category){
        return postService.findAllByCategory(category);
    }

    /** 게시글 저장 */
    @PostMapping("/starTalk/newPost")
    public void savePost(@RequestBody SavePostDto postDto) {
        postService.save(postDto);
        System.out.println("'" + postDto.getTitle() + "'" + "게시글이 저장됐습니다.");
    }

    /** 게시글 수정 */
    @PostMapping("/starTalk/updatePost/{postId}")
    public void updatePost(@PathVariable("postId") Long postId, @RequestBody SavePostDto postDto) {
        postService.update(postId, postDto);
        System.out.println(postId + "번 게시글이 수정됐습니다.");
    }

    /** 게시글 삭제 */
    @DeleteMapping("/starTalk/deletePost/{postId}")
    public void deletePost(@PathVariable("postId") Long postId) {
        postService.delete(postId);
        System.out.println(postId + "번 게시글이 삭제됐습니다.");
    }

}
