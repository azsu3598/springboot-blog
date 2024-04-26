package me.projects.bootblog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import me.projects.bootblog.domain.Kakao.KakaoInfo;
import me.projects.bootblog.dto.KakaoToken;
import me.projects.bootblog.domain.User;
import me.projects.bootblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.beans.Encoder;
import java.util.UUID;

@Service
public class KakaoApiService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;

    public String KakaoGet(String code, String url){
        RestTemplate restTemplate = new RestTemplate();

        // POST 요청에 필요한 데이터 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "1f477e3568aeeec5dd54574ed1e80ccd");
        body.add("client_secret", "JVFxWXCZ12OzGEcpCgHTTGrEw1JpNH4e");
        body.add("redirect_uri", "http://localhost:8080/oauth2/callback/kakao");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // POST 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        // Json Simple
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoToken kakaoToken = null;
        try {
            kakaoToken = objectMapper.readValue(responseEntity.getBody(), KakaoToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        // 추가내용
        // 응답 처리
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // 성공적으로 액세스 토큰을 받은 경우
            String responseBody = responseEntity.getBody();
            return responseBody;
        } else {
            // 오류 발생 시 처리
            return null;
        }
    }
    public KakaoInfo getUserInfo(String token){
        final String KAKAO_API_URL = "https://kapi.kakao.com/v2/user/me";

        // HTTP 요청을 보내기 위한 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        // HTTP 요청 객체 생성
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Kakao API의 /v2/user/me 엔드포인트로 GET 요청 전송
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                KAKAO_API_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoInfo kakaoInfo = null;
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());
        try {
            kakaoInfo = objectMapper.readValue(responseEntity.getBody(), KakaoInfo.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        System.out.println("kakaoInfo.getId() = " + kakaoInfo.getId());
        System.out.println(kakaoInfo.getProperties());
        System.out.println("kakaoInfo.getKakao_account().email = " + kakaoInfo.getKakao_account().email);
        // 응답의 본문(body)을 반환
        return kakaoInfo;
    }

    public String extractAccessToken(String token) {
        // JSON 문자열을 JsonObject로 변환
        JsonObject jsonObject = new Gson().fromJson(token, JsonObject.class);

        // access_token 필드의 값을 추출
        String accessToken = jsonObject.get("access_token").getAsString();

        return accessToken;
    }
    public User parseUserInfo(String jsonResponse) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        JsonNode propertiesNode = jsonNode.get("properties");
        String nickname = propertiesNode.get("nickname").asText();

        JsonNode kakaoAccountNode = jsonNode.get("kakao_account");
        String email = kakaoAccountNode.get("email").asText();

        return new User(nickname, email);
    }

    // 추가
    public User KakaoUser(KakaoInfo kakaoInfo){
        UUID garbagePwd = UUID.randomUUID();
        String enpwd = bCryptPasswordEncoder.encode(garbagePwd.toString());
        return User.builder()
                .email(kakaoInfo.getKakao_account().getEmail())
                .nickname(kakaoInfo.getProperties().getNickname())
                .password(enpwd)
                .build();
    }

}