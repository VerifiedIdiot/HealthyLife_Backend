package com.HealthCare.HealthyLife_Backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "delete_member_tb")
public class DeleteMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delete_member_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private Member member;
}
