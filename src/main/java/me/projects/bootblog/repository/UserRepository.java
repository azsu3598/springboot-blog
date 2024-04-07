package me.projects.bootblog.repository;

import me.projects.bootblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   // 이메릴로 사용자 정보를 가져옴
}
