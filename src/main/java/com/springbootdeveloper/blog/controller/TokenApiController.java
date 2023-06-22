package com.springbootdeveloper.blog.controller;

import com.springbootdeveloper.blog.domain.RefreshToken;
import com.springbootdeveloper.blog.dto.CreateAccessTokenRequest;
import com.springbootdeveloper.blog.dto.CreateAccessTokenResponse;
import com.springbootdeveloper.blog.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;

@RequiredArgsConstructor
@Controller
@RequestMapping("/token")
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/new")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request){

        // 새 액세스 토큰 생성
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
