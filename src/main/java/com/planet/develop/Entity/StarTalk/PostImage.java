package com.planet.develop.Entity.StarTalk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** 게시글 이미지 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 글 외래키 (다대일 양방향)

    private String image; // 이미지
}
