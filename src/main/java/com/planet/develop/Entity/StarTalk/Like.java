package com.planet.develop.Entity.StarTalk;

import com.planet.develop.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** 좋아요 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Like {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long likeId; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 사용자 외래키 (다대일)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 글 외래키 (다대일)

}
