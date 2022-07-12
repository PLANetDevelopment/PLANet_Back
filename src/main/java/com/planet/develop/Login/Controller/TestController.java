package com.planet.develop.Login.Controller;

import com.planet.develop.Login.JWT.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@RestController
public class TestController {

    // 사용자 ID 받아오기
    @GetMapping("/header")
    public void requestSomething2(@RequestHeader(JwtProperties.USER_ID) String value) {
        System.out.println(value);
    }

}
