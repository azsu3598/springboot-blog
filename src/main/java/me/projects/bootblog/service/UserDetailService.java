package me.projects.bootblog.service;

import lombok.RequiredArgsConstructor;
import me.projects.bootblog.domain.User;
import me.projects.bootblog.repository.UserRepository;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override // 사용자 이름(email)로 사용자의 정보를 가져오는 메서드
    public User loadUserByUsername(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));
    }

}
