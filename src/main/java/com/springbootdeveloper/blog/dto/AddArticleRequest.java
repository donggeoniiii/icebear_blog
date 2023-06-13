package com.springbootdeveloper.blog.dto;

import com.springbootdeveloper.blog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest { // 게시글을 생성하기 위한 정보와 함께 요청하는 dto

    private String title;
    private String content;

    // 빌더 패턴을 이용해 게시글 객체 생성
    public Article toEntity() {
        return Article.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
