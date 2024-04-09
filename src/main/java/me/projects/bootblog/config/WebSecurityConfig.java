package me.projects.bootblog.config;


import lombok.RequiredArgsConstructor;
import me.projects.bootblog.config.jwt.TokenProvider;
import me.projects.bootblog.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final UserDetailService userDetailService;


    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) ->     web.ignoring()
                    .requestMatchers(toH2Console())
                    .requestMatchers("/static/**");
    }

    // 특정 Http 요청에 대한 웹 기반 보안 구성
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/login", "/signup", "/user").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin((formLogin) -> formLogin
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/articles"));
//
//        http
//                .logout((logout) ->
//                        logout.logoutSuccessUrl("/login")
//                                .invalidateHttpSession(true)
//                );
//
//        http.csrf((csrf) -> csrf.disable());
//
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                //authorizeRequest() deprecated 오류 해결(링크 : https://sennieworld.tistory.com/109)
                .authorizeHttpRequests()// 인증 인가 설정
                .requestMatchers("/login", "/signup", "/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()// 폼 기반 로그인 설정
                .loginPage("/login")
                .defaultSuccessUrl("/articles")
                .and()
                .logout()//로그아웃 설정
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .and()
                .csrf().disable() //csrf 비활성화
                .build();
    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//
//        daoAuthenticationProvider.setUserDetailsService(userDetailService);
//        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
//
//        return daoAuthenticationProvider;
//    }
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception{
    return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailService) // 8. 사용자 정보 서비스 설정
            .passwordEncoder(bCryptPasswordEncoder)
            .and()
            .build();
}

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
