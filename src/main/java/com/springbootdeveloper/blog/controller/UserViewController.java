package com.springbootdeveloper.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class UserViewController {

    // 로그인 페이지로 연결
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // 회원가입 페이지로 연결
    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
}
