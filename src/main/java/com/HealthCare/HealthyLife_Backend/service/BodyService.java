package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.BodyDto;
import com.HealthCare.HealthyLife_Backend.entity.Body;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.repository.BodyRepository;
import com.HealthCare.HealthyLife_Backend.repository.CalendarRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BodyService {
    private final BodyRepository bodyRepository;
    private final MemberRepository memberRepository;
    private final CalendarRepository calendarRepository;

    public boolean saveBody(BodyDto bodyDto) {
        try {
            System.out.println("응답값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! : " + bodyDto.getEmail());
            Body body = Body.builder()

                    .member(memberRepository.findByEmail(bodyDto.getEmail())
                            .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다.")))
                    .height(bodyDto.getHeight())
                    .weight(bodyDto.getWeight())
                    .muscle(bodyDto.getMuscle())
                    .fat(bodyDto.getFat())
                    .fatPercent(bodyDto.getFatPercent())
                    .bmr(bodyDto.getBmr())
                    .bmi(bodyDto.getBmi())
                    .DCI(bodyDto.getDCI())
                    .build();

            bodyRepository.save(body);

            Member member = memberRepository.findByEmail(bodyDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
            // 인바디정보가 insert 되는 순간 캘린더에도 생성이 되거나 update 되게 한다
            updateOrInsertCalendar(member, body);


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return date.format(formatter);
    }




    private void updateOrInsertCalendar(Member member, Body body) {
        String formattedDate = formatDate(body.getDate()); // LocalDate를 YYYYmmDD 형식으로 변환

        List<Calendar> calendars = calendarRepository.findByMember(member);
        calendars.forEach(calendar -> {
            // Calendar의 regDate와 Body의 date 비교
            if (calendar.getRegDate().compareTo(formattedDate) >= 0) {
                // 조건을 만족하는 경우에만 Body 정보 업데이트
                calendar.setBody(body);
            }
            calendarRepository.save(calendar);
        });

        // Calendar 레코드가 없는 경우 새로 생성하는 로직은 변경 없음
//        if (calendars.isEmpty()) {
//            Calendar calendar = new Calendar();
//            calendar.setMember(member);
//            calendar.setBody(body);
//            calendar.setRegDate(formattedDate); // 여기도 형식 맞춰서 설정
//            calendarRepository.save(calendar);
//        }
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