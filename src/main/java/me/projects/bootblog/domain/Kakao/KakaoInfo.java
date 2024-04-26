package me.projects.bootblog.domain.Kakao;

import lombok.Data;

@Data
public class KakaoInfo {

    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

}