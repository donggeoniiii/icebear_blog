package com.springbootdeveloper.blog.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity // 엔티티로 설정
@Getter // 게터 생성
@Setter // 세터 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본생성자 생성, 접근범위는 protected
public class Article {

    @Id // 기본키로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 설정
    @Column(name = "id", nullable = false, updatable = false) // column 이름, not null, unique, 수정불가 설정
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder // 빌더 패턴으로 게시글 객체 생성
    /*
    빌더패턴 예시

     Article.builder()
             .title("제목")
     .content("내용")
     .build();

    이런 식으로 만들 수 있음
    어느 필드에 어느 값이 들어가는 지 가독성이 좋아짐
     */
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }


    /*
    게시글 수정 메소드
    JpaRepository 인터페이스 안에 있는 update 메소드를 통해
    article 테이블에 있는 해당 id 레코드 수정하기
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
