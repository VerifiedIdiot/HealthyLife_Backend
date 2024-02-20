package com.HealthCare.HealthyLife_Backend.entity;



import com.HealthCare.HealthyLife_Backend.dto.SeasonRankingDto;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import javax.persistence.*;


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

    @Builder.Default
    @Column(nullable = false)
    private Integer ranks = 1;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "total_ranking_id")
    private TotalRanking totalRanking;

    @Column(nullable = false)
    @Builder.Default
    private Integer points = 0;

    private String regDate;

    public SeasonRankingDto toDto() {
        return SeasonRankingDto.builder()
                .id(this.getId())
                .memberId(this.getMember().getId())
                .ranks(this.getRanks())
                .nickname(this.getMember().getNickName())
                .gender(this.getMember().getGender())
                .regDate(this.getRegDate())
                .points(this.getPoints())
                .build();
    }

}
