package com.HealthCare.HealthyLife_Backend.service;


import com.HealthCare.HealthyLife_Backend.dto.MemberStatusDto;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.MemberStatus;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberStatusService {
    private final MemberRepository memberRepository;
    private final MemberStatusRepository memberStatusRepository;

    // 상태 변경
    public MemberStatus changeStatus(Long memberId, String newStatus) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));
        MemberStatus memberStatus = memberStatusRepository.findByMember(member);
        if (memberStatus == null) {
            // MemberStatus가 없는 경우 새로 생성하여 추가
            memberStatus = new MemberStatus();
            memberStatus.setMember(member);
            memberStatus.setStatus(newStatus);
        } else {
            // MemberStatus가 있는 경우 상태를 변경
            memberStatus.setStatus(newStatus);
        }
        return memberStatusRepository.save(memberStatus);
    }

    // 최근 접속 시간 업데이트
    public MemberStatus updateLastAccessTime(Long memberId, LocalDateTime newLastAccessTime) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));
        MemberStatus memberStatus = memberStatusRepository.findByMember(member);
        memberStatus.setLastAccessTime(newLastAccessTime);
        return memberStatusRepository.save(memberStatus);
    }

    // 상태 메세지 변경
    public MemberStatus updateStatusMessage(Long memberId, String newStatusMessage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));
        MemberStatus memberStatus = memberStatusRepository.findByMember(member);
        if (memberStatus == null) {
            // MemberStatus가 없는 경우 새로 생성하여 추가
            memberStatus = new MemberStatus();
            memberStatus.setMember(member);
            memberStatus.setStatusMessage(newStatusMessage);
        } else {
            // MemberStatus가 있는 경우 상태를 변경
            memberStatus.setStatusMessage(newStatusMessage);
        }
        memberStatus.setStatusMessage(newStatusMessage);
        return memberStatusRepository.save(memberStatus);
    }

    // 회원정보로 상태메세지, 최근접속시간, 상태 출력
    public MemberStatusDto getMemberStatusInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));
        MemberStatus memberStatus = memberStatusRepository.findByMember(member);

        if (memberStatus == null) {
            // MemberStatus가 없을 경우 예외처리 또는 기본값 설정 등을 수행할 수 있습니다.
            throw new EntityNotFoundException("MemberStatus not found for member with id: " + memberId);
        }
        return MemberStatusDto.builder()
                .statusMessage(memberStatus.getStatusMessage())
                .lastAccessTime(memberStatus.getLastAccessTime())
                .status(memberStatus.getStatus())
                .memberId(member)
                .build();
    }

}


