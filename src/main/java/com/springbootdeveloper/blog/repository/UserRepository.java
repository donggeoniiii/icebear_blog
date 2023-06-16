package com.springbootdeveloper.blog.repository;

import com.springbootdeveloper.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 유저 정보 찾을 예정
    Optional<User> findByEmail(String email); // 단건이니까 optional

    // 이 밖에도 findByAOrB, findByAAndB, findByALess(Greater)Than, findByA(is)Null 등..
}
