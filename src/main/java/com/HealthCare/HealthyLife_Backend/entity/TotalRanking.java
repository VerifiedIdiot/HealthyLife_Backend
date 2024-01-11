package com.HealthCare.HealthyLife_Backend.entity;


import com.HealthCare.HealthyLife_Backend.dto.RankingDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

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
    @Column(name = "total_ranking_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nick_name", unique = true , nullable = false)
    private String nickName;

    @Column(unique = true , nullable = false)
    private String gender;

    @Column(nullable = false)
    private Long points;

    @Column(nullable = true)
    private Integer rank;

    public RankingDto toDto() {
        return RankingDto.builder()
                .id(this.getId())
                .nickName(this.getNickName())
                .gender(this.getGender())
                .points(this.getPoints())
                .rank(this.getRank())
                .build();
    }

}
