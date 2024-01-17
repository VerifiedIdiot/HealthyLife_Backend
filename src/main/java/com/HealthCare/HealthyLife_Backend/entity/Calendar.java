package com.HealthCare.HealthyLife_Backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.xwpf.usermodel.IBody;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "calendar_tb")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Calendar {
    @Id
    @Column(name = "calendar_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "calendar_title",nullable = false)
    private String calendarTitle; //제목

    @Column(name = "calendar_detail",length = 1000)
    private String calendarDetail; // 내용

    @Column(name = "calendar_write_date",nullable = false)
    private LocalDate diaryWriteDate; //작성일자

    @Column(name = "points")
    private Integer points; // 포인트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id")
    private Body body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @OneToMany(mappedBy = "calendar" , cascade = CascadeType.ALL)
    private List<SeasonRanking> seasonRankings;
}
