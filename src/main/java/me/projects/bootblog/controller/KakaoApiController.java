package me.projects.bootblog.controller;

import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
//import me.projects.bootblog.service.KakaoApiService;
import lombok.extern.slf4j.Slf4j;
import me.projects.bootblog.domain.User;
import me.projects.bootblog.repository.UserRepository;
import me.projects.bootblog.service.KakaoApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
@RequiredArgsConstructor
@RestController
public class KakaoApiController {
    private final UserRepository userRepository;
    private final KakaoApiService kakaoApiService;
    @Description("회원이 소셜 로그인을 마치면 자동으로 실행되는 API입니다. 인가 코드를 이용해 사용자 정보를 " +
            "이용하여 서비스에 회원가입합니다.")
    @RequestMapping(value = "oauth2/callback/kakao")
    public ResponseEntity<String> KakaoLogin(@RequestParam("code") String code) {
        final String url = "https://kauth.kakao.com/oauth/token";
        String token = kakaoApiService.KakaoGet(code, url);
        String access_token = kakaoApiService.extractAccessToken(token);
        String result = kakaoApiService.getUserInfo(access_token);

        try {
            User kakaoUser = kakaoApiService.parseUserInfo(result);
            User user = userRepository.findByEmail(kakaoUser.getEmail())
                    .map(entity -> entity.update(kakaoUser.getNickname()))
                    .orElse(User.builder()
                            .email(kakaoUser.getNickname())
                            .password("") // Set a default or null password
                            .nickname(kakaoUser.getEmail())
                            .build());
            userRepository.save(user);
            return ResponseEntity.ok("User information saved successfully");
        }catch (Exception e) {
            // Handle parsing or other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to parse user information");
        }
    }

}
