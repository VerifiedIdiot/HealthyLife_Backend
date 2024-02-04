package com.HealthCare.HealthyLife_Backend.entity.calendar;

import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meal_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Meal {
    @Id
    @Column(name = "meal_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String mealType; // 식사유형 (아침,점심,저녁)
    private String mealName;
    private String memberId;
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }

    public MealDto toMealDto(Meal meal) {
        return MealDto.builder()
                .mealType(this.getMealType())
                .mealName(this.getMealName())
                .memberId(this.getMemberId())
                .build();
    }

}
