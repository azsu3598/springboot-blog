package me.projects.bootblog.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.projects.bootblog.domain.Article;
import me.projects.bootblog.dto.AddArticleRequest;
import me.projects.bootblog.dto.UpdateArticleRequest;
import me.projects.bootblog.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor    // final 이 붙거나 @Notnull이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request, String userName){
        return blogRepository.save(request.toEntity(userName));
    }

    public List<Article> findAll(){
        return blogRepository.findAll();
    }
    public Article findById(Long id){
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found" + id));
    }
    public void delete(Long id){
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found" + id));

        article.update(request.getTitle(), request.getContent());
        return article;
    }
}
