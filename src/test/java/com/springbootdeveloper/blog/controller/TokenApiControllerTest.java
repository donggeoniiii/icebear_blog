package com.springbootdeveloper.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootdeveloper.blog.config.jwt.JwtFactory;
import com.springbootdeveloper.blog.config.jwt.JwtProperties;
import com.springbootdeveloper.blog.domain.RefreshToken;
import com.springbootdeveloper.blog.domain.User;
import com.springbootdeveloper.blog.dto.CreateAccessTokenRequest;
import com.springbootdeveloper.blog.repository.RefreshTokenRepository;
import com.springbootdeveloper.blog.repository.UserRepository;
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

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    
    @BeforeEach
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        userRepository.deleteAll();
    }
    
    @DisplayName("액세스 토큰 발급하기")
    @Test
    void createNewAccessToken() throws Exception {
        // given
        final String url = "/token/new";

        // 테스트용 유저
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        // 리프레시 토큰
        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken(jwtProperties);

        // 토큰 생성해놓고
        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

        // access token 생성 요청 생성
        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);
        final String requestBody = objectMapper.writeValueAsString(request);

        // when: 액세스 토큰 생성
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isCreated()) // 생성메시지 받고
                .andExpect(jsonPath("$.accessToken").isNotEmpty()); // access token이 response에 있어야


    }
}