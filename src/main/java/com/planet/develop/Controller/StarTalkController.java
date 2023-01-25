package com.planet.develop.Controller;

import com.planet.develop.DTO.PostDto.PostDto;
import com.planet.develop.DTO.PostDto.SaveCommentDto;
import com.planet.develop.DTO.PostDto.SavePostDto;
import com.planet.develop.Entity.StarTalk.Comment;
import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Entity.StarTalk.Reply;
import com.planet.develop.Entity.StarTalk.Scrap;
import com.planet.develop.Entity.User;
import com.planet.develop.Login.JWT.JwtProperties;
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

    /** 테스트 용 ------------------------------------------------------------------ */

    /** 전체 게시글 가져오기 */
    @GetMapping("/starTalk")
    public PostDto findAll() {
        List<Post> postList = postService.findAll();
        return new PostDto(postList);
    }

//    /** 전체 게시글 가져오기 */
//    @GetMapping("/starTalk2")
//    public List<Post> findAll2() {
//        final List<Post> post = postService.findAll();
//        System.out.println(post.get(0));
//        System.out.println(post.get(1));
//        return postService.findAllPostAndComment(post);
//    }

    /** 내 게시글 가져오기 */
    @GetMapping("/starTalk/myPost")
    public PostDto findAllByUser(@RequestHeader(JwtProperties.USER_ID) String userId) {
        User user = userRepository.findById(userId).get();
        List<Post> allByUser = postService.findAllByUser(user);
        return new PostDto(allByUser);
    }

    /** 스크랩 게시글 가져오기 */
    @GetMapping("/starTalk/scrap")
    public List<Scrap> findAllByScrap(@RequestHeader(JwtProperties.USER_ID) String userId) {
        User user = userRepository.findById(userId).get();
        return postService.findAllByScrap(user);
    }

    /** 스크랩하기 */
    @PostMapping("/starTalk/scrap/{postId}")
    public void saveScrap(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("postId") Long postId) {
        postService.scrap(postId, userId);
        System.out.println(postId + "번 게시글이 스크랩 됐습니다.");
    }

    /** 스크랩 취소 */
    @PostMapping("/starTalk/deleteScrap/{postId}")
    public void deleteScrap(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("postId") Long postId) {
        postService.canceScrap(postId, userId);
        System.out.println(postId + "번 게시글 스크랩이 취소됐습니다.");
    }

    /** 내가 쓴 댓글 */
    @GetMapping("/starTalk/myComment")
    public List<Comment> findMyComment(@RequestHeader(JwtProperties.USER_ID) String userId) {
        return postService.findMyComment(userId);
    }

    /** 내가 쓴 대댓글 */
    @GetMapping("/starTalk/myReply")
    public List<Reply> findMyReply(@RequestHeader(JwtProperties.USER_ID) String userId) {
        return postService.findMyReply(userId);
    }

    /** 카테고리별 게시글 가져오기 */
    @GetMapping("/starTalk/{category}")
    public PostDto savePost(@PathVariable("category") String category) {
        List<Post> allByCategory = postService.findAllByCategory(category);
        return new PostDto(allByCategory);
    }

    /** 게시글 검색하기 */
    @GetMapping("/starTalk/retrieve")
    public PostDto retreivePost(@RequestParam("query") String query) {
        List<Post> posts = postService.retrieveAll(query);
        return new PostDto(posts);
    }

    /** 게시글 저장 */
    @PostMapping("/starTalk/newPost")
    public void savePost(@RequestHeader(JwtProperties.USER_ID) String userId, @RequestBody SavePostDto postDto) {
        postDto.setUserId(userId);
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
    @PostMapping("/starTalk/deletePost/{postId}")
    public void deletePost(@PathVariable("postId") Long postId) {
        postService.delete(postId);
        System.out.println(postId + "번 게시글이 삭제됐습니다.");
    }

    /** 댓글 달기 */
    @PostMapping("/starTalk/newComment/{postId}")
    public void saveComment(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("postId") Long postId, @RequestBody SaveCommentDto commentDto) {
        commentDto.setUserId(userId); commentDto.setPostId(postId);
        postService.comment(commentDto);
        System.out.println(commentDto.getPostId() + "번 게시글에 댓글이 달렸습니다.");
    }

    /** 댓글 수정 */
    @PostMapping("/starTalk/updateComment/{commentId}")
    public void updateComment(@RequestHeader(JwtProperties.USER_ID) String userId,
                              @PathVariable("commentId") Long commentId, @RequestBody SaveCommentDto commentDto) {
        postService.updateComment(commentId, commentDto);
        System.out.println(commentId + "번 게시글에 댓글이 수정됐습니다.");
    }

    /** 댓글 삭제 */
    @PostMapping("/starTalk/deleteComment/{commentId}")
    public void deleteComment(@PathVariable("commentId") Long commentId) {
        postService.deleteComment(commentId);
        System.out.println(commentId + "번 댓글이 삭제됐습니다.");
    }

    /** 좋아요 */
    @PostMapping("/starTalk/saveLike/{postId}")
    public void saveLike(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("postId") Long postId) {
        postService.like(postId, userId);
        System.out.println(postId + "번 게시글에 좋아요가 달렸습니다.");
    }

    /** 좋아요 취소 */
    @PostMapping("/starTalk/deleteLike/{postId}")
    public void deleteLike(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("postId") Long postId) {
        postService.cancelLike(userId, postId);
        System.out.println(postId + "번 게시글 좋아요가 취소됐습니다.");
    }

}
