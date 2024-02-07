package com.HealthCare.HealthyLife_Backend.entity.calendar;

import com.HealthCare.HealthyLife_Backend.dto.calendar.MealDto;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Column(name = "meal_type")
    private String mealType; // 식사유형 (아침,점심,저녁)

    @Column(name = "meal_name")
    private String mealName;

    private String email;

    @Column(name = "reg_date")
    private String regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @PrePersist
    public void prePersist() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.regDate = LocalDateTime.now().format(formatter);
    }
    public MealDto toMealDto() {
        return MealDto.builder()
                .mealType(this.getMealType())
                .mealName(this.getMealName())
                .email(this.getEmail())
                .regDate(this.getRegDate())
                .build();
    }

}
