package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.dto.RankingDto;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "season_ranking_tb")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@DynamicInsert
public class SeasonRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "season_ranking_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "total_ranking_id")
    private TotalRanking totalRanking;

    @Column(nullable = false)
    private Long points;

    @Column(nullable = true)
    private Integer ranking; // 시즌 종료 시의 랭킹 순위

    public RankingDto toDto() {
        return RankingDto.builder()
                .id(this.getId())
                .member(this.getMember())
                .points(this.getPoints())
                .ranking(this.getRanking())
                .build();
    }
}
