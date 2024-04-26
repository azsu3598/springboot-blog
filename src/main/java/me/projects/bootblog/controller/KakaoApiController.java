package me.projects.bootblog.controller;

import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
//import me.projects.bootblog.service.KakaoApiService;
import lombok.extern.slf4j.Slf4j;
import me.projects.bootblog.domain.Kakao.KakaoInfo;
import me.projects.bootblog.domain.User;
import me.projects.bootblog.repository.UserRepository;
import me.projects.bootblog.service.KakaoApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
@RequiredArgsConstructor
//@RestController
@Controller
public class KakaoApiController {
    private final UserRepository userRepository;
    private final KakaoApiService kakaoApiService;

    @Description("회원이 소셜 로그인을 마치면 자동으로 실행되는 API입니다. 인가 코드를 이용해 사용자 정보를 " +
            "이용하여 서비스에 회원가입합니다.")
    @RequestMapping(value = "oauth2/callback/kakao")
    public String KakaoLogin(@RequestParam("code") String code, Model model) {
        final String url = "https://kauth.kakao.com/oauth/token";
        String token = kakaoApiService.KakaoGet(code, url);
        String access_token = kakaoApiService.extractAccessToken(token);
        KakaoInfo result = kakaoApiService.getUserInfo(access_token);

        try {
            User kakaoUser = kakaoApiService.KakaoUser(result);
            User user = userRepository.findByEmail(kakaoUser.getEmail())
                    .map(entity -> entity.update(kakaoUser.getNickname()))
                    .orElse(User.builder()
                            .email(kakaoUser.getEmail())
                            .password(kakaoUser.getPassword())
                            .nickname(kakaoUser.getNickname())
                            .build());
            userRepository.save(user);

            // Return the URL of the articles page
            return "redirect:/articles";
        } catch (Exception e) {
            // Handle parsing or other exceptions
            model.addAttribute("error", "An error occurred during login. Please try again."); // Add error message to model
            return "redirect:/login"; // Redirect to the login page with error message
        }
    }
}