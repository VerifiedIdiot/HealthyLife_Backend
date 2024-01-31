package com.HealthCare.HealthyLife_Backend.entity.calendar;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lunch_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Lunch {

    @Id
    @Column(name = "lunch_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    private String lunch; // 아침

    private LocalDateTime regDate;
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }
}