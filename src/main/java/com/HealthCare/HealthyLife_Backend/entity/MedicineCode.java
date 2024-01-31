package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineCodeDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "medicine_code_tb") // 실제 데이터베이스 테이블 이름에 맞게 지정해야 합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineCode {

    @Id
    @Column(name = "medicine_code_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long medicineCodeId;

    private String type;

    private String id;

    private String functionality;




    public MedicineCodeDto toDto() {
        return MedicineCodeDto.builder()
                .type(this.getType())
                .id(this.getId())
                .functionality(this.getFunctionality())
                .build();
    }
}
