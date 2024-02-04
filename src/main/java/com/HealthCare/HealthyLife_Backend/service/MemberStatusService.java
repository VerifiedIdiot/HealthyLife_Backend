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
        memberStatus.setStatus(newStatus);
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
        memberStatus.setStatusMessage(newStatusMessage);
        return memberStatusRepository.save(memberStatus);
    }

    // 회원정보로 상태메세지, 최근접속시간, 상태 출력
    public MemberStatusDto getMemberStatusInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));

        MemberStatus memberStatus = memberStatusRepository.findByMember(member);
        if (memberStatus == null) {
            throw new EntityNotFoundException("MemberStatus not found for Member with id: " + memberId);
        }

        return MemberStatusDto.builder()
                .statusMessage(memberStatus.getStatusMessage())
                .lastAccessTime(memberStatus.getLastAccessTime())
                .status(memberStatus.getStatus())
                .memberId(member)
                .build();
    }

}


