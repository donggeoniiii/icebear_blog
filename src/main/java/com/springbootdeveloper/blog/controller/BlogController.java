package com.springbootdeveloper.blog.controller;

import com.springbootdeveloper.blog.domain.Article;
import com.springbootdeveloper.blog.dto.AddArticleRequest;
import com.springbootdeveloper.blog.dto.UpdateArticleRequest;
import com.springbootdeveloper.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor // final이 붙는 필드에 의존성 주입, @Autowired 어노테이션 필요없음
public class BlogController {

    private final BlogService blogService;

    /*
    블로그 글 쓰기 API
     */
    @PostMapping("/article")
    public ResponseEntity<Article> registArticle(@RequestBody AddArticleRequest request) {

        // 게시글 생성
        Article article = blogService.regist(request);

        // 생성된 게시글과 함께 201 status 전달
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(article);
    }

    /*
    블로그 글 전체 목록 보기 API
     */
    @GetMapping("/article")
    public ResponseEntity<List<Article>> showAllArticles(){

        // 게시글 목록 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(blogService.findAll());
    }

    /*
    블로그 글 상세 보기 API
     */
    @GetMapping("/article/{id}")
    public ResponseEntity<Article> showArticle(@PathVariable Long id){

        // 게시글 정보 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(blogService.findById(id));
    }

    /*
    블로그 글 수정하기 API
     */
    @PutMapping("/article/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {

        // 게시글 수정
        Article updatedArticle = blogService.update(id, request);

        // 수정된 게시글 정보 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedArticle);
    }

    /*
    블로그 글 삭제하기 API
     */
    @DeleteMapping("/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){

        // 게시글 삭제
        blogService.delete(id);

        // 삭제되었다고 알림
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
