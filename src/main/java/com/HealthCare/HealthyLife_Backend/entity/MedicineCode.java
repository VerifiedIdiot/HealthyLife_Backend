package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.dto.MedicineCodeDto;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "medicine_code_tb")
public class MedicineCode {

    @Id
    @Column(name ="medicine_code_id" )
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name ="type")
    private String medicineType;
    @Column(name ="code")
    private String medicineCode;
    @Column(name ="functionality")
    private String functionality;

    @ElementCollection
    @CollectionTable(name = "medicine_materials", joinColumns = @JoinColumn(name = "medicine_code_id"))
    @Column(name ="materials")
    private List<String> materials;



    public MedicineCodeDto toDto() {
        return MedicineCodeDto.builder()
                .medicineType(this.getMedicineType())
                .medicineCode(this.getMedicineCode())
                .functionality(this.getFunctionality())
                .materials(this.getMaterials())
                .build();
    }

}
