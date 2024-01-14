package com.HealthCare.HealthyLife_Backend.service;


import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailService implements UserDetailsService {
    private MemberRepository memberRepository;

    @Override
    // 로그인 시 이메일을 통해 DB에서 회원 정보를 가져온다. createUserDetails() 메서드를 통해 UserDetails 타입으로 변환한다.
    // UserDetails는 Spring Security에서 사용자의 인증 및 권한 부여를 위한 인터페이스로, 사용자 정보를 담고 있음
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return memberRepository.findByEmail(username)
                    .map(this::createUserDetails)
                    .orElseThrow(() -> new UsernameNotFoundException(username + "을 DB에서 찾을 수 없습니다."));
    }
    private UserDetails createUserDetails(Member member) {
        // 권한 정보를 문자열로 변환
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
        // UserDetails 타입의 객체를 생성해 리턴
        return new User(
                String.valueOf(member.getId()),  // 사용자 식별자 (여기서는 ID를 문자열로 사용)
                member.getPassword(),      // 사용자 비밀번호
                Collections.singleton(grantedAuthority)  // 사용자 권한 (여기서는 단일 권한을 사용)
        );
    }
}
