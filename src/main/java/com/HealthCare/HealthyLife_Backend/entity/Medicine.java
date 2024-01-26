package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.dto.MedicineDto;
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
@Table(name = "medicine_tb")
public class Medicine {

    @Id
    @Column(name ="medicine_id" )
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "report_no", unique = true)
    private String reportNo;

    @Column(name = "name")
    private String name;

    @Column(name = "company")
    private String company;

    @ElementCollection
    @CollectionTable(name = "medicine_functionalities", joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "functionality")
    private List<String> functionalities;

    @ElementCollection
    @CollectionTable(name = "medicine_materials", joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "material")
    private List<String> materials;



    public MedicineDto toDto() {
        return MedicineDto.builder()
                .type(this.getType())
                .reportNo(this.getReportNo())
                .name(this.getName())
                .company(this.getCompany())
                .functionalities(this.getFunctionalities())
                .materials(this.getFunctionalities())
                .build();
    }

}
