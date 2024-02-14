package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.MemberReqDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberResDto;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 상세 조회
    public MemberResDto getMemberDetail(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()-> new RuntimeException("해당 회원이 존재하지 않습니다."));
        return MemberResDto.of(member);
    }
    // 비밀번호 일치 체크
    public boolean isPassword(String password, Long id) {
        log.info("password : {}", password);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
        boolean isPw = passwordEncoder.matches(password, member.getPassword());
        log.info("isPw : {}", isPw);
        return isPw;
    }

    // 회원 정보 수정
    public boolean modifyMember(MemberReqDto memberReqDto) {
        log.info("Password : {} ", memberReqDto.getPassword());
        try {
            Member member = memberRepository.findByEmail(memberReqDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
            if(!member.isKakao() && !memberReqDto.getPassword().isEmpty()) {
                member.setPassword(passwordEncoder.encode(memberReqDto.getPassword()));
            }
            member.setNickName(memberReqDto.getNickName());
            member.setPhone(memberReqDto.getPhone());
            member.setAddr(memberReqDto.getAddr());
            member.setImage(memberReqDto.getImage());
            memberRepository.save(member);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // 회원 탈퇴
//    public boolean deleteMember(Long id) {
//        try {
//            Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
//            member.setAuthDelete(true);
//        }
//    }

    // 이메일 출력
    public String getEmail(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
        );

        return member.getEmail();
    }

}
