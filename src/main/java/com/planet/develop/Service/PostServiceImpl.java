package com.planet.develop.Service;

import com.planet.develop.DTO.PostDto.SaveCommentDto;
import com.planet.develop.DTO.PostDto.SavePostDto;
import com.planet.develop.Entity.StarTalk.Comment;
import com.planet.develop.Entity.StarTalk.Like;
import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Entity.User;
import com.planet.develop.Repository.CommentRepository;
import com.planet.develop.Repository.LikeRepository;
import com.planet.develop.Repository.PostRepository;
import com.planet.develop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /** 게시글 저장 */
    @Override
    public void save(SavePostDto post) {
        postRepository.save(post.dtoToEntity());
    }

    /** 게시글 수정 */
    @Override
    public void update(Long postId, SavePostDto post) {
        Post byPostId = postRepository.findByPostId(postId);
        byPostId.update(post);
    }

    /** 게시글 삭제 */
    @Override
    public void delete(Long postId) {
        postRepository.deleteByPostId(postId);
    }

    /** 전체 게시글 가져오기 */
    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    /** 사용자별 게시글 가져오기 */
    @Override
    public List<Post> findAllByUser(User user) {
        return postRepository.findAllByUser(user);
    }

    /** 카테고리별 게시글 가져오기 */
    @Override
    public List<Post> findAllByCategory(String category) {
        return postRepository.findAllByCategory(category);
    }

    /** 좋아요 순으로 정렬하기 */
    @Override
    public List<Post> sortByLike() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCount"));
    }

    /** 오래된 순으로 정렬하기 */
    @Override
    public List<Post> sortByOldest() {
        return postRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
    }

    /** 최근 순으로 정렬하기 */
    @Override
    public List<Post> sortByNewest() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    /** 좋아요 */
    @Override
    public void like(Long postId, String userId) {
        // post 테이블에 좋아요 수 증가
        Post post = postRepository.findByPostId(postId);
        post.like();
        // like 테이블에 데이터 삽입
        Like like = Like.builder()
                .post(post)
                .user(userRepository.findById(userId).get())
                .build();
        likeRepository.save(like);
        System.out.println("좋아요를 눌렀습니다.");
    }

    /** 좋아요 취소 */
    @Override
    public void cancelLike(Long likeId) {
        // post 테이블에 좋아요 수 줄이기
        Like like = likeRepository.findByLikeId(likeId);
        Post post = postRepository.findByPostId(like.getPost().getPostId());
        post.cancelLike();
        // like 테이블에서 데이터 삭제하기
        likeRepository.delete(like);
        System.out.println("좋아요를 취소했습니다.");
    }

    /** 댓글 달기 */
    @Override
    public void comment(SaveCommentDto dto) {
        // post 테이블에 댓글 수 증가
        Post post = postRepository.findByPostId(dto.getPostId());
        post.comment();
        // comment 테이블에 데이터 삽입
        Comment comment = dto.dtoToEntity();
        commentRepository.save(comment);
        System.out.println("댓글을 달았습니다.");
    }

    /** 댓글 수정 */
    @Override
    public void updateComment(SaveCommentDto dto) {
        Comment comment = commentRepository.findByCommentId(dto.getCommentId());
        comment.update(dto);
    }

    /** 댓글 삭제 */
    @Override
    public void deleteComment(Long commentId) {
        // post 테이블에 댓글 수 줄이기
        Comment comment = commentRepository.findByCommentId(commentId);
        Post post = postRepository.findByPostId(comment.getPost().getPostId());
        post.cancelComment();
        // comment 테이블에서 데이터 삭제하기
        commentRepository.delete(comment);
        System.out.println("댓글을 삭제했습니다");
    }

}
