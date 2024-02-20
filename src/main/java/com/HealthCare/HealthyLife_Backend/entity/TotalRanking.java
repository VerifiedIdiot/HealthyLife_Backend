package com.HealthCare.HealthyLife_Backend.entity;



import com.HealthCare.HealthyLife_Backend.dto.TotalRankingDto;
import com.HealthCare.HealthyLife_Backend.utils.Views;
import com.fasterxml.jackson.annotation.*;
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

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @Column(nullable = false)
    private Integer ranks = 1;

    @Builder.Default
    @OneToMany(mappedBy = "totalRanking" , cascade = CascadeType.ALL)
    @JsonView(Views.List.class)
    private List<SeasonRanking> seasonRankings = new ArrayList<>();


    @Column(nullable = false)
    @Builder.Default
    private Integer points = 0;


    public TotalRankingDto toDto() {
        return TotalRankingDto.builder()
                .id(this.getId())
                .memberId(this.getMember().getId())
                .ranks(this.getRanks())
                .nickname(this.getMember().getNickName())
                .gender(this.getMember().getGender())
                .points(this.getPoints())
                .build();
    }

    public void addSeasonRanking(SeasonRanking seasonRanking) {
        this.seasonRankings.add(seasonRanking);
        seasonRanking.setTotalRanking(this);
    }




}
