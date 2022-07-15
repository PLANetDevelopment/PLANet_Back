package com.planet.develop.Login.Controller;

import com.planet.develop.Entity.User;
import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Login.Model.OauthToken;
import com.planet.develop.Login.Service.KakaoUserService;
import com.planet.develop.Repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {

    private final KakaoUserService kakaoUserService;
    private final UserRepository userRepository;

    public UserController(KakaoUserService kakaoUserService, UserRepository userRepository) {
        this.kakaoUserService = kakaoUserService;
        this.userRepository = userRepository;
    }

    @GetMapping("/oauth/token") // 프론트에서 인가코드 받아오는 url
    public String getLogin(@RequestParam("code") String code) {
        // 넘어온 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = kakaoUserService.getAccessToken(code);

        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        String jwtToken = kakaoUserService.saveUserAndGetToken(oauthToken.getAccess_token());

        // 응답 헤더의 Authorization 항목에 JWT를 넣음
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        String userId = kakaoUserService.getUserId(oauthToken.getAccess_token());
        String nickname = kakaoUserService.getNickname(userId);

        User user = User.builder()
                .userId(userId)
                .nickname(nickname)
                .build();

        // User tbl에도 정보를 저장하도록
        userRepository.save(user);

        return userId;
    }

}