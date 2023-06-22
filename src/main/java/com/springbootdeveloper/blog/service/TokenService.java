package com.springbootdeveloper.blog.service;

import com.springbootdeveloper.blog.config.jwt.TokenProvider;
import com.springbootdeveloper.blog.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    // 리프레시 토큰으로 새 액세스 토큰 발급하기
    public String createNewAccessToken(String refreshToken){
        if (!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2L));
    }

}
