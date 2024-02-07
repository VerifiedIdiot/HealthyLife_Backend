package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.MemberReqDto;
import com.HealthCare.HealthyLife_Backend.dto.MemberResDto;
import com.HealthCare.HealthyLife_Backend.security.SecurityUtil;
import com.HealthCare.HealthyLife_Backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.HealthCare.HealthyLife_Backend.utils.Common.CORS_ORIGIN;

@Slf4j
@RestController
@RequestMapping("/member")
//@CrossOrigin(origins = CORS_ORIGIN)
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final SecurityUtil securityUtil;

    // 회원 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<MemberResDto> memberDetail(){
        Long id = securityUtil.getCurrentMemberId();
        log.info("id : {} ", id);
        MemberResDto memberResDto = memberService.getMemberDetail(id);
        return ResponseEntity.ok(memberResDto);
    }

    //토큰값받고 이메일 출력
    @GetMapping("/takenEmail")
    public ResponseEntity<String> takenEmail() {
        String email=memberService.getEmail(securityUtil.getCurrentMemberId());
        return ResponseEntity.ok(email);
    }

    @GetMapping("/takenId")
    public ResponseEntity<Long> takenMemberId(){
        Long id = securityUtil.getCurrentMemberId();
        return ResponseEntity.ok(id);
    }

    // 회원 정보 조회
    @GetMapping("/detail/{memberId}")
    public ResponseEntity<MemberResDto> memberInfo(@PathVariable Long memberId){
        MemberResDto memberResDto = memberService.getMemberDetail(memberId);
        return ResponseEntity.ok(memberResDto);
    }

    //비밀번호 체크
    @PostMapping("/isPassword")
    public ResponseEntity<Boolean> checkPw(@RequestBody Map<String, String> data){
        log.info("연결완료 ? " + data);
        Long id = SecurityUtil.getCurrentMemberId();
        String password = data.get("password");
        Boolean isTrue = memberService.isPassword(password, id);
        return ResponseEntity.ok(isTrue);
    }

    //회원 정보 수정
    @PostMapping("/update")
    public ResponseEntity<Boolean> updateMember(@RequestBody MemberReqDto memberReqDto){
        log.info("memberReqDto : {}", memberReqDto);
        return ResponseEntity.ok(memberService.modifyMember(memberReqDto));
    }
}