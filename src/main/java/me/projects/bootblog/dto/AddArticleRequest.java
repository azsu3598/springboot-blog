package me.projects.bootblog.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.projects.bootblog.domain.Article;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    private String title;
    private String content;
    private String author;
    public Article toEntity(){
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
