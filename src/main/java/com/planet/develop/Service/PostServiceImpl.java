package com.planet.develop.Service;

import com.planet.develop.DTO.PostDto.SavePostDto;
import com.planet.develop.Entity.StarTalk.Post;
import com.planet.develop.Entity.User;
import com.planet.develop.Repository.PostRepository;
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

}
