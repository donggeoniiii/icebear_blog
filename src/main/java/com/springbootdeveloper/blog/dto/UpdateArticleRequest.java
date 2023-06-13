package com.springbootdeveloper.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateArticleRequest { // 게시글을 수정하기 위한 요청을 보내는 dto
    private String title;
    private String content;
}
