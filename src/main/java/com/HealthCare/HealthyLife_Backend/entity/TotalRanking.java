package com.HealthCare.HealthyLife_Backend.entity;


import com.HealthCare.HealthyLife_Backend.dto.RankingDto;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "total_ranking_tb")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
public class TotalRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "total_ranking_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "totalRanking" , cascade = CascadeType.ALL)
    private List<SeasonRanking> seasonRankings = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private Integer points = 0;

    public RankingDto toDto() {
        return RankingDto.builder()
                .id(this.getId())
                .points(this.getPoints())
                .build();
    }

    public void addSeasonRanking(SeasonRanking seasonRanking) {
        this.seasonRankings.add(seasonRanking);
        seasonRanking.setTotalRanking(this);
    }

}
