package com.springbootdeveloper.blog.service;

import com.springbootdeveloper.blog.domain.Article;
import com.springbootdeveloper.blog.dto.AddArticleRequest;
import com.springbootdeveloper.blog.dto.UpdateArticleRequest;
import com.springbootdeveloper.blog.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    /*
    게시글 entity 생성 메소드
    JpaRepository 인터페이스 안에 있는 save 메소드를 통해
    AddArticleRequest 클래스의 값들을 Article 테이블에 저장
     */
    public Article regist(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    /*
    게시글 목록 조회 메소드
    JpaRepository 인터페이스 안에 있는 findAll 메소드를 통해
    article 테이블에 있는 모든 레코드 가져오기
     */
    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    /*
    게시글 상세 보기 메소드
    JpaRepository 인터페이스 안에 있는 findById 메소드를 톻해
    article 테이블에 있는 해당 id 레코드 가져오기
     */
    public Article findById(Long id){
        return blogRepository.findById(id)
                // 없을 시 예외 발생
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id)); //
    }

    /*
    게시글 수정하기 메소드
    JpaRepository 인터페이스 안에 findById로 게시글을 찾은 다음(없음 예외처리)
    article 테이블에 있는 해당 id 레코드 수정하기
     */
    @Transactional // 트랜잭션, 글이 없으면 수정도 안되어야 하니까
    public Article update(Long id, UpdateArticleRequest request){
        // 일단 글이 있는지 보고
        Article updatedArticle = blogRepository.findById(id)
                // 없을 시 예외발생
                .orElseThrow(() -> new IllegalArgumentException("not found: " + request.getTitle()));

        // 글 수정
        updatedArticle.update(request.getTitle(), request.getContent());

        // 수정된 글 정보 반환
        return updatedArticle;
    }

    /*
    게시글 삭제 메소드
    JpaRepository 인터페이스 안에 있는 deleteById 메소드를 통해
    article 테이블에 있는 해당 id 레코드 삭제하기
     */
    public void delete(Long id) {
        blogRepository.deleteById(id);
    }
}
