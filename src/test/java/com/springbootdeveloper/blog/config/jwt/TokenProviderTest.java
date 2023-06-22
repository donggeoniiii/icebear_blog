package com.springbootdeveloper.blog.config.jwt;

import com.springbootdeveloper.blog.domain.User;
import com.springbootdeveloper.blog.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    // 토큰 생성
    @DisplayName("토큰 생성하기")
    @Test
    void generateToken(){
        // given: 토큰에 들어갈 유저 정보
        User testUser = userRepository.save(User.builder()
                .email("user@email.com")
                .password("test")
                .build());

        // when: 토큰 생성
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14L));

        // then
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        // jwt를 통해 얻은 유저 id와 처음에 입력한 값이 같아야 함
        Assertions.assertThat(userId).isEqualTo(testUser.getId());
    }

    // 토큰 검증: 실패
    @DisplayName("토큰 검증하기_실패")
    @Test
    void invalidToken(){
        // given: 토큰 정보
        String token = JwtFactory.builder()
                // 시간 조정해서 이미 만료된 토큰으로 생성
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7L).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when: 토큰 검증 결과
        boolean result = tokenProvider.validToken(token);

        // then
        Assertions.assertThat(result).isFalse();
    }

    // 토큰 검증: 성공
    @DisplayName("토큰 검증하기_성공")
    @Test
    void validToken(){
        // given: 토큰 정보
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // when: 토큰 검증 결과
        boolean result = tokenProvider.validToken(token);

        // then
        Assertions.assertThat(result).isTrue();
    }

    // 토큰으로 인증정보 가져오기
    @DisplayName("인증정보 가져오기")
    @Test
    void getAuthentication(){
        // given: 토큰 생성
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                // username: 이메일로 설정
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // when: 권한 부여
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then: 가져온 username이 곧 입력한 이메일과 같아야 함
        Assertions.assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    // 유저 아이디로 검증하기
    @DisplayName("getUserId() 테스트")
    @Test
    void getUserId(){
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // when: 메소드 테스트
        Long userIdByToken = tokenProvider.getUserId(token);

        // then: 토큰에서 가져온 아이디가 처음에 설정한 값과 같아야 함
        Assertions.assertThat(userIdByToken).isEqualTo(userId);
    }
}
