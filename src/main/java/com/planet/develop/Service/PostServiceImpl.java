package com.planet.develop.Service;

import com.planet.develop.DTO.PostDto.PostAndCommentDto;
import com.planet.develop.DTO.PostDto.SaveCommentDto;
import com.planet.develop.DTO.PostDto.SavePostDto;
import com.planet.develop.Entity.StarTalk.*;
import com.planet.develop.Entity.User;
import com.planet.develop.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final ReplyRepository replyRepository;

    public Post postDtoToEntity(SavePostDto dto) {
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(dto.getCategory())
                .user(userRepository.findById(dto.getUserId()).get())
                .likeCount(0)
                .commentCount(0)
                // 이미지 코드 추가
                .build();
        post.changeDate(LocalDate.now());
        return post;
    }

    public Comment commentDtoToEntity(SaveCommentDto dto) {
        Comment comment = Comment.builder()
                .post(postRepository.findByPostId(dto.getPostId()))
                .comment(dto.getComment())
                .user(userRepository.findById(dto.getUserId()).get())
                .build();
        return comment;
    }

    public LikeTbl makeLikeEntity(Long postId, String userId) {
        return LikeTbl.builder()
                .post(postRepository.findById(postId).get())
                .user(userRepository.findById(userId).get())
                .build();
    }

    /** 게시글 저장 */
    @Override
    public void save(SavePostDto post) {
        postRepository.save(postDtoToEntity(post));
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

    /** 게시글 & 댓글 가져오기 */
    @Override
    public List<PostAndCommentDto> findAllPostAndComment() {
        List<Post> postList = postRepository.findAll();
        System.out.println("게시글 리스트 입니다=====================================");
        for (Post p : postList) {
            System.out.println(p);
            System.out.println(commentRepository.countAllByPost(p.getPostId()));
        }
        List<PostAndCommentDto> pacList = new ArrayList<>();
        postList.stream().forEach((post -> {
            Long commentCount = commentRepository.countAllByPost(post.getPostId()); // 댓글 수
            System.out.println("댓글수 함수가 실행됐습니다=========================");
            System.out.println(commentCount);
            Long likeCount = likeRepository.countAllByPost(post.getPostId()); // 좋아요 수
            System.out.println("좋아요수 함수가 실행됐습니다=========================");
            System.out.println(likeCount);
            List<Comment> commentList = commentRepository.findByPost(post); // 댓글 리스트
            System.out.println("댓글 리스트 함수가 실행됐습니다=========================");
            System.out.println(commentList);

            PostAndCommentDto dto =
                    PostAndCommentDto.builder()
                    .post(post)
                    .commentCount(commentCount)
                    .likeCount(likeCount)
                    .comment(commentList)
                    .build();
            pacList.add(dto);
        }));
        return pacList;
    }

    /** 사용자별 게시글 가져오기 */
    @Override
    public List<Post> findAllByUser(User user) {
        return postRepository.findAllByUser(user);
    }

    /** 스크랩한 게시글 가져오기 */
    @Override
    public List<Scrap> findAllByScrap(User user) {
        List<Scrap> scrapList = scrapRepository.findAllByUser(user);
        return scrapList;
    }

    /** 스크랩하기 */
    @Override
    public void scrap(Long postId, String userId) {
        Scrap scrap = Scrap.builder()
                .post(postRepository.findByPostId(postId))
                .user(userRepository.findByUserId(userId))
                .build();
        scrapRepository.save(scrap);
    }

    /** 스크랩 취소하기 */
    @Override
    public void canceScrap(Long postId, String userId) {
        Scrap scrap = scrapRepository.findByPostAndUser(postRepository.findByPostId(postId), userRepository.findByUserId(userId));
        scrapRepository.delete(scrap);
    }

    /** 내가 쓴 댓글 */
    @Override
    public List<Comment> findMyComment(String userId) {
        return commentRepository.findAllByUser(userRepository.findByUserId(userId));
    }

    /** 내가 쓴 대댓글 */
    @Override
    public List<Reply> findMyReply(String userId) {
        return replyRepository.findAllByUser(userRepository.findByUserId(userId));
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
        likeRepository.save(makeLikeEntity(postId, userId));
    }

    /** 좋아요 취소 */
    @Override
    public void cancelLike(String userId, Long postId) {
        // post 테이블에 좋아요 수 줄이기
        LikeTbl likeTbl = likeRepository.findByUserAndPost(userRepository.findByUserId(userId),
                postRepository.findByPostId(postId));
        Post post = postRepository.findByPostId(postId);
        post.cancelLike();
        // like 테이블에서 데이터 삭제하기
        likeRepository.delete(likeTbl);
        System.out.println("좋아요를 취소했습니다.");
    }

    /** 댓글 달기 */
    @Override
    public void comment(SaveCommentDto dto) {
        // post 테이블에 댓글 수 증가
        Post post = postRepository.findByPostId(dto.getPostId());
        post.comment();
        // comment 테이블에 데이터 삽입
        Comment comment = commentDtoToEntity(dto);
        commentRepository.save(comment);
    }

    /** 댓글 수정 */
    @Override
    public void updateComment(Long commentId, SaveCommentDto dto) {
        Comment comment = commentRepository.findByCommentId(commentId);
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

    /**
     * 질의어 '+' 기준으로 자르기
     */
    @Override
    public String[] splitQuery(String query) {
        return query.split(" ");
    }

    /**
     * 하나의 단어로 제목 검색 -> List<PostId> 반환
     */
    @Override
    public List<Long> retrieveTitle(String query) {
        List<Post> posts = postRepository.findByTitleContaining(query);
        ArrayList<Long> postIdList = new ArrayList<>();
        posts.stream().forEach(i -> postIdList.add(i.getPostId()));
        return postIdList;
    }

    /**
     * 하나의 단어로 본문 검색 -> List<PostId> 반환
     */
    @Override
    public List<Long> retrieveContent(String query) {
        List<Post> posts = postRepository.findByContentContaining(query);
        ArrayList<Long> postIdList = new ArrayList<>();
        posts.stream().forEach(i -> postIdList.add(i.getPostId()));
        return postIdList;
    }

    /**
     * 전체 질의어로 제목 검색
     */
    @Override
    public List<Long> retrieveAllTitle(String[] querys) {
        List<Long> postIdList = new ArrayList<>();
        for (String query : querys){
            List<Long> result = retrieveTitle(query);
            postIdList.addAll(result);
        }
        return postIdList;
    }

    /**
     * 전체 질의어로 본문 검색
     */
    @Override
    public List<Long> retrieveAllContent(String[] querys) {
        List<Long> postIdList = new ArrayList<>();
        for (String query : querys){
            List<Long> result = retrieveContent(query);
            postIdList.addAll(result);
        }
        return postIdList;
    }

    /**
     * (제목 검색 결과) OR (본문 검색 결과)
     */
    @Override
    public List<Post> retrieveAll(String query) {
        String[] querys = splitQuery(query);

        List<Long> postIdOfTitle = retrieveAllTitle(querys); // 제목에서 찾기
        List<Long> postIdOfContent = retrieveAllContent(querys); // 내용에서 찾기

        List<Long> idList = new ArrayList<>();
        idList.addAll(postIdOfTitle); idList.addAll(postIdOfContent); // 제목 + 내용에서 찾기
        List<Long> result = idList.stream().distinct().collect(Collectors.toList()); // 중복 제거
        return postIdToPost(result);
    }

    /** List<Long> postIdList -> List<Post> postList 변환 */
    @Override
    public List<Post> postIdToPost(List<Long> postId) {
        List<Post> postList = new ArrayList<>();
        postId.stream().forEach(id -> postList.add(postRepository.findByPostId(id)));
        return postList;
    }

}
