package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.dto.RankingDto;
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

    @Column(name = "nick_name",nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private int season; // 해당 시즌

    @Column(nullable = false)
    private Long points;

    @Column(nullable = true)
    private Integer ranking; // 시즌 종료 시의 랭킹 순위

    @Column(name = "season_end_date", nullable = false)
    private LocalDate seasonEndDate; // 시즌 종료 날짜

    public RankingDto toDto() {
        return RankingDto.builder()
                .id(this.getId())
                .nickName(this.getNickName())
                .gender(this.getGender())
                .points(this.getPoints())
                .ranking(this.getRanking())
                .season(this.getSeason())
                .seasonEndDate(this.getSeasonEndDate())
                .build();
    }
}
