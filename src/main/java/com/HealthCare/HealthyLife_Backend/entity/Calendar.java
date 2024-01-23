package com.HealthCare.HealthyLife_Backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_tb") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Calendar {
    @Id
    @Column(name = "calendarId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "calendar_title",nullable = false)
    private String calendarTitle; //제목

    @Column(name = "calendar_detail",length = 1000)
    private String calendarDetail; // 내용

    @Column(name = "calendar_write_date",nullable = false)
    private LocalDate diaryWriteDate; //작성일자
}
