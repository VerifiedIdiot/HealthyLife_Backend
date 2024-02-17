package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.dto.RankingDto;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "season_ranking_tb")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
public class SeasonRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "season_ranking_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "total_ranking_id")
    private TotalRanking totalRanking;

    @Column(nullable = false)
    @Builder.Default
    private Integer points = 0;


    private String regDate;

    public RankingDto toDto() {
        return RankingDto.builder()
                .id(this.getId())
                .points(this.getPoints())
                .regDate(this.getRegDate())
                .build();
    }
}
