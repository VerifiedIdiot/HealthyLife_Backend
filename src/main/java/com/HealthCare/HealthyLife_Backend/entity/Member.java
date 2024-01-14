package com.HealthCare.HealthyLife_Backend.entity;

import com.HealthCare.HealthyLife_Backend.enums.Authority;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private String password;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "alias", nullable = false, unique = true)
    private String alias;
    @Column(name = "gender", nullable = false)
    private String gender;
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;
    @Column(name = "addr", nullable = false)
    private String addr;
    private String image;
    @Column(name = "isPayment", columnDefinition = "TINYINT(1)")
    private boolean isPayment;
    @Column(name = "isAuthDelete", columnDefinition = "TINYINT(1)")
    private boolean isAuthDelete;
    private LocalDate birth;
    private LocalDateTime regDate;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @PrePersist
    protected void prePersist() {
        regDate = LocalDateTime.now();
    }

    @Builder
    public Member(String email, String password, String name, String alias, String gender, String phone
                 , String addr, String image, LocalDate birth, boolean isPayment, boolean isAuthDelete, Authority authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.alias = alias;
        this.gender = gender;
        this.phone = phone;
        this.addr = addr;
        this.image = image;
        this.birth = birth;
        this.regDate = LocalDateTime.now();
        this.isPayment = isPayment;
        this.isAuthDelete = isAuthDelete;
        this.authority = authority;
    }
}
