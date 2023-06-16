package com.springbootdeveloper.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootdeveloper.blog.domain.Article;
import com.springbootdeveloper.blog.dto.AddArticleRequest;
import com.springbootdeveloper.blog.dto.UpdateArticleRequest;
import com.springbootdeveloper.blog.repository.BlogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 테스트용 ApplicationContext
@AutoConfigureMockMvc // MockMvc 생성
class BlogControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();

        // 테스트 전 h2 db에 뭔가 있었으면 삭제하고 시작
        blogRepository.deleteAll();
    }

    @DisplayName("registArticle: 블로그 게시글 생성")
    @Test
    public void registArticleTest() throws Exception {
        // given : 테스트 상황 설정
        final String url = "/api/article";
        final String title = "제목";
        final String content = "내용";
        final AddArticleRequest request = new AddArticleRequest(title, content);

        // 테스트를 위해 JSON 직렬화
        final String requestBody = objectMapper.writeValueAsString(request);

        // when: 테스트할 api 실행시켜보기
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated()); // 일단 생성신호(201)이 왔는지 확인

        List<Article> articles = blogRepository.findAll(); // 리스트를 받아서

        assertThat(articles.size()).isEqualTo(1); // 길이가 1인지 확인(얘 하나만 나와야 하니까)
        assertThat(articles.get(0).getTitle()).isEqualTo("제목"); // 제목이 생각한게 맞는지 확인
        assertThat(articles.get(0).getContent()).isEqualTo("내용"); // 내용이 생각한게 맞는지 확인

    }

    @DisplayName("showAllArticles: 게시글 목록 조회")
    @Test
    public void showAllArticles() throws Exception {
        // given
        final String url = "/api/article";
        final String title = "title";
        final String content = "content";

        // repository에 테스트 정보 저장
        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }

    @DisplayName("showArticle: 게시글 상세 보기")
    @Test
    public void showArticle() throws Exception {
        // given
        final String url = "/api/article/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    @DisplayName("updateArticle: 게시글 수정")
    @Test
    public void updateArticle() throws Exception {
        // given
        final String url = "/api/article/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final String newTitle = "new title";
        final String newContent = "new content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    @DisplayName("deleteArticle: 게시글 삭제")
    @Test
    public void deleteArticle() throws Exception {
        // given
        final String url = "/api/article/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isNoContent());

        // then
        List<Article> articles = blogRepository.findAll();

        // 비어있나 확인
        assertThat(articles).isEmpty();
    }



}