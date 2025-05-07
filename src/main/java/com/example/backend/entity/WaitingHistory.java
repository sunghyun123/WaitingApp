package com.example.backend.entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //고객(id,phoneNumber)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;


    //대기 번호
    @Column(nullable = false)
    private Integer waitingNumber;

    //예상 대기 시간
    @Column(nullable = false)
    private Integer expectedWaitingTime;

    //대기 상태
    @Column(nullable = false)
    private Boolean isCompleted = false;

    //웨이팅 시간
    @Column(nullable = false)
    private Integer WaitingTime;

    //웨이팅 시작 시간
    @Column(nullable = false)
    private LocalDateTime createdAt;

    //웨이팅 완료 시간
    private LocalDateTime completedAt;
}
