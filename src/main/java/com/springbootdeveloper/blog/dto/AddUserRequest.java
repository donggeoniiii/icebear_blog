package com.springbootdeveloper.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    // 로그인에 사용할 email
    private String email;

    // 로그인에 사용할 비밀번호
    private String password;
}
