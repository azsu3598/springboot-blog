package me.projects.bootblog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import me.projects.bootblog.domain.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoApiService {

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
    public String getUserInfo(String token){
        final String KAKAO_API_URL = "https://kapi.kakao.com/v2/user/me";

        // HTTP 요청을 보내기 위한 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 요청 객체 생성
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Kakao API의 /v2/user/me 엔드포인트로 GET 요청 전송
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                KAKAO_API_URL,
                HttpMethod.GET,
                requestEntity,
                String.class);

        // 응답의 본문(body)을 반환
        return responseEntity.getBody();
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

}