package com.HealthCare.HealthyLife_Backend.entity;


import com.HealthCare.HealthyLife_Backend.dto.ChatMessageDto;
import com.HealthCare.HealthyLife_Backend.enums.MessageType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "Chatting")
public class Chatting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType type;
    private String message; //메세지
    private LocalDateTime messageTime; //메세지 생성 시간

}
