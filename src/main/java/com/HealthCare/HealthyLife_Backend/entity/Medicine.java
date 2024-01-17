package com.HealthCare.HealthyLife_Backend.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Blob;


@Entity
@Table(name = "medicine_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@DynamicInsert
public class Medicine {

    @Id
    @Column(name = "medicine_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true , nullable = false)
    private String name;

    @Column(unique = true , nullable = false)
    private String company;

    @Lob
    @Column(nullable = false)
    private String functionalities;
}
