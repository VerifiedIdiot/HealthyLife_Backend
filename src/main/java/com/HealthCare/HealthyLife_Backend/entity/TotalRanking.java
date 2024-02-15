package com.HealthCare.HealthyLife_Backend.entity;


import com.HealthCare.HealthyLife_Backend.dto.RankingDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "total_ranking_tb")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@DynamicInsert
public class TotalRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "total_ranking_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private Member member;

    @OneToMany(mappedBy = "totalRanking" , cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<SeasonRanking> seasonRankings;

    @Column(nullable = false)
    private Long points;

    @Column(nullable = true)
    private Integer ranking;

    public RankingDto toDto() {
        return RankingDto.builder()
                .id(this.getId())
                .points(this.getPoints())
                .ranking(this.getRanking())
                .build();
    }

}
