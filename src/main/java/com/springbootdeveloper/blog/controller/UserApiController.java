package com.springbootdeveloper.blog.controller;

import com.springbootdeveloper.blog.dto.AddUserRequest;
import com.springbootdeveloper.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    // 회원 가입
    @PostMapping("/user")
    public String signup(AddUserRequest request){
        System.out.println("되니");
        
        // 회원가입 진행
        userService.regist(request);

        // 로그인 페이지로 이동
        return "redirect:/login";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler()
                // 로그아웃 담당 핸들러의 logout 메소드로 로그아웃
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        
        return "redirect:/login";
    }

}
