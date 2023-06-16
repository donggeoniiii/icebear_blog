package com.springbootdeveloper.blog.config;

import com.springbootdeveloper.blog.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure(){
        return web -> web.ignoring()
                .requestMatchers(toH2Console()) // h2 console로 처리하는 모든 request에 대해
                .requestMatchers("/static/**"); // 모든 경로로 오는 request에 대해
    }

    // http request에 대한 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 인증, 인가 설정
                .authorizeRequests()
                .requestMatchers("/login", "/signup", "/user").permitAll() // 해당 페이지는 모두 접근 가능
                .anyRequest().authenticated() // 그 외 페이지는 인가는 필요 없어도 인증은 필요할 수 있음
                .and()

                // 로그인 설정
                .formLogin() // 이번엔 이메일 / pw 입력하는 form으로 하니까
                .loginPage("/login") // 페이지는 '/login' 에서
                .defaultSuccessUrl("/article") // 로그인하면 여기로
                .and()

                // 로그아웃 설정
                .logout()
                .logoutSuccessUrl("/login") // 로그아웃하면 여기로
                .invalidateHttpSession(true) // 로그아웃 이후 세션 만료 여부(현재 true)
                .and()

                // csrf 공격 방지(현재 비활성화)
                .csrf().disable()
                .build();
    }

    // 인증 관리자 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                // 사용자 정보를 가져올 서비스 설정(userDetailsService implement)
                .userDetailsService(userService)
                // 비밀번호 암호화를 위한 인코더
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    // 비밀번호 인코더로 빈으로 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
