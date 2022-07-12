package com.planet.develop.Login.Model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;

@Entity
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_code")
    private Long userCode;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "kakao_profile_img")
    private String kakaoProfileImg;

    @Column(name = "kakao_nickname")
    private String kakaoNickname;

    @Column(name = "kakao_email")
    private String kakaoEmail;

    @Column(name = "user_role")
    private String userRole;

//    @Builder
//    public User(Long kakaoId, String kakaoProfileImg, String kakaoNickname,
//                String kakaoEmail, String userRole) {
//        this.kakaoId = kakaoId;
//        this.kakaoProfileImg = kakaoProfileImg;
//        this.kakaoNickname = kakaoNickname;
//        this.kakaoEmail = kakaoEmail;
//        this.userRole = userRole;
//    }

}
