package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.BodyDto;
import com.HealthCare.HealthyLife_Backend.entity.Body;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.BodyRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BodyService {
    private final BodyRepository bodyRepository;
    private final MemberRepository memberRepository;

    public boolean saveBody(BodyDto bodyDto) {
        try {
            System.out.println("응답값 : " + bodyDto.getMemberEmail());
            Body body = Body.builder()

                    .member(memberRepository.findByEmail(bodyDto.getMemberEmail())
                            .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다.")))
                    .height(bodyDto.getHeight())
                    .weight(bodyDto.getWeight())
                    .muscle(bodyDto.getMuscle())
                    .fat(bodyDto.getFat())
                    .fatPercent(bodyDto.getFatPercent())
                    .bmr(bodyDto.getBmr())
                    .bmi(bodyDto.getBmi())
                    .build();

            bodyRepository.save(body);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BodyDto> getBodyByEmail(String email) {
        List<Body> bodies = bodyRepository.findByMemberEmail(email);
        List<BodyDto> bodyDtos = new ArrayList<>();
        for (Body body : bodies) {
            bodyDtos.add(body.toBodyDto()); // 수정된 부분
        }
        return bodyDtos;
    }
}

