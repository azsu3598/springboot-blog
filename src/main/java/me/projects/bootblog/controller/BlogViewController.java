package me.projects.bootblog.controller;


import lombok.RequiredArgsConstructor;
import me.projects.bootblog.domain.Article;
import me.projects.bootblog.dto.ArticleListViewResponse;
import me.projects.bootblog.dto.ArticleViewResponse;
import me.projects.bootblog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model){
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles);
        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model){
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }
    @GetMapping("new-article")
    // id 키를 가진 쿼리 파라미터의 값을 id 변수에 매핑(id 없을 수도 있음)
    public String newArticle(@RequestParam(required = false) Long id, Model model){
        if(id == null){ // id값 없으면 생성
            model.addAttribute("article", new ArticleViewResponse());
        }
        else{ // id 값 있으면 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }
}
