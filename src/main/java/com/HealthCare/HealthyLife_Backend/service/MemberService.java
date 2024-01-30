package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.MemberReqDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberResDto;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 상세 조회
    public MemberResDto getMemberDetail(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
        return converEntityToDto(member);
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

            memberRepository.save(Member.builder()
                    .nickName(memberReqDto.getNickName())
                    .password(memberReqDto.getPassword().isEmpty()
                            ? member.getPassword()
                            : passwordEncoder.encode(memberReqDto.getPassword()))
                    .phone(memberReqDto.getPhone())
                    .addr(memberReqDto.getAddr())
                    .image(memberReqDto.getImage())
                    .build());
            return true;
        } catch (Exception e) {
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

    // 회원 엔티티를 회원 DTO로 변환
    private MemberResDto converEntityToDto(Member member) {
        return MemberResDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickName(member.getNickName())
                .gender(member.getGender())
                .phone(member.getPhone())
                .addr(member.getAddr())
                .image(member.getImage())
                .birth(member.getBirth())
                .regDate(member.getRegDate())
                .build();
    }
}
