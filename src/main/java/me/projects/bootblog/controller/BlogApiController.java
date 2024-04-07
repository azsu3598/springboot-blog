package me.projects.bootblog.controller;


import lombok.RequiredArgsConstructor;
import me.projects.bootblog.domain.Article;
import me.projects.bootblog.dto.AddArticleRequest;
import me.projects.bootblog.dto.ArticleResponse;
import me.projects.bootblog.dto.UpdateArticleRequest;
import me.projects.bootblog.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogApiController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    //@RequestBody로 요청 본문 값 매핑
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
        Article savedArticle = blogService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticle(){
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/articles/{id}")
    // @PathVariable -> url경로에서 id에 해당하는 값을 받아줌
    // @PathVartiable 시에 오류 발생하면 name 값을 지정
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable(name = "id") Long id){
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable(name = "id") Long id){
        blogService.delete(id);
        return ResponseEntity.ok()
                .build();

    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request){
        Article updateArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updateArticle);
    }
}
