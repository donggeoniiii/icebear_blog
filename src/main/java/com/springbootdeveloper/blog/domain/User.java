package com.springbootdeveloper.blog.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String password, String auth){
        this.email = email;
        this.password = password;
    }

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 유저 이름 반환(여기선 이메일로)
    @Override
    public String getUsername() {
        return this.email;
    }

    /*
        유저 pw 변환하는 메소드도 있다. 물론 그대로 알려주면 안되고 암호화해야.
     */

    // 계정 만료 여부 반환(일단 그냥 true로)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부 반환(일단 그냥 true로)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정의 패스워드 만료 여부 반환(일단 그냥 true로)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 사용 가능여부 반환(일단은 true로)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
