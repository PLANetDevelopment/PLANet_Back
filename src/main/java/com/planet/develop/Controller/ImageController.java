package com.planet.develop.Controller;

import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class ImageController {

    private final FileService fileService;

    @PostMapping("/image/upload")
    public void uploadImage(@RequestHeader(JwtProperties.USER_ID) String userId, @RequestPart MultipartFile file) throws IOException {
        System.out.println("========================================================");
        System.out.println("파일: " + file);
        System.out.println("바이트: " + file.getBytes());
        System.out.println("콘텐트 타입: " + file.getContentType());
        System.out.println("인풋스트림: " + file.getInputStream());
        System.out.println("이름: " + file.getName());
        System.out.println("리소스: " + file.getResource());
        System.out.println("오리지널 파일 이름: " + file.getOriginalFilename());
        System.out.println("클래스: " + file.getClass());

        fileService.fileUpload(file);
    }

}
