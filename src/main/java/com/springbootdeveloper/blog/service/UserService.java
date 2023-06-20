package com.springbootdeveloper.blog.service;

import com.springbootdeveloper.blog.domain.User;
import com.springbootdeveloper.blog.dto.AddUserRequest;
import com.springbootdeveloper.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    // user 관련 repository
    private final UserRepository userRepository;

    // 비밀번호 암호화
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입 메소드
    public Long regist(AddUserRequest dto){
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                // 비밀번호 암호화
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                // 생성 후 할당된 id 반환
                .build()).getId();
    }


}
