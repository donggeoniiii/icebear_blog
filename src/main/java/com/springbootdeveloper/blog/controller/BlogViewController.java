package com.springbootdeveloper.blog.controller;

import com.springbootdeveloper.blog.domain.Article;
import com.springbootdeveloper.blog.dto.ArticleDetailViewResponse;
import com.springbootdeveloper.blog.dto.ArticleListViewResponse;
import com.springbootdeveloper.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogViewController {

    private final BlogService blogService;

    // 전체 게시글 보기
    @GetMapping("/article")
    public String getArticles(Model model){
        // 블로그 글 긁어오기
        List<ArticleListViewResponse> articleList = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        // 블로그 글 목록 model에 담아서 view로 보내기
        model.addAttribute("articleList", articleList);

        // articleList.html 찾기
        return "articleList";
    }

    // 게시글 상세 보기
    @GetMapping("/article/{id}")
    public String getArticle(@PathVariable Long id, Model model){
        Article article = blogService.findById(id);

        // 찾는 게시글 정보 model에 담아서 view로 보내기
        model.addAttribute("article", article);

        // articleDetail.html 찾기
        return "articleDetail";
    }

    // 게시글 생성 및 수정 폼 가져오기
    @GetMapping("/article/new")
    public String registArticle(@RequestParam(required = false) Long id, Model model){

        // id가 없으면
        if (id == null) {
            // 게시글 생성, model에 생성한 게시글 객체(껍데기) 추가
            model.addAttribute("article", new ArticleDetailViewResponse());
            System.out.println("생성");
        }
        else {
            // 있으면 해당하는 id를 가진 게시글( == 수정할 게시글 정보 가져오기)
            Article article = blogService.findById(id);
            System.out.println("수정");
            System.out.println(article.getId());

            // model에 수정할 게시글 정보 추가
            model.addAttribute("article", new ArticleDetailViewResponse(article));
        }

        return "articleCreate";
    }
}
