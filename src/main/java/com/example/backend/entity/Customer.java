package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private int waitingNumber_day;

    //웨이팅 번호 서버 자동 할당
    @Column(nullable = false)
    private int waitingNumber;

    //동반 인원 수
    @Column(nullable = false)
    private int people;

    //대기 시작 시간
    @Column(nullable = false)
    private String startTime;

    //웨이팅 시간
    @Column(nullable = false)
    private int wait; // 예상 대기 시간 (분)

    //현재 입장 상태, entered, waiting, cancel, done
    @Column(nullable = false)
    private String status;

    //입장 종료 시간
    @Column(nullable = false)
    private String enteredTime;

    //오늘 날짜
    @Column(nullable = false)
    private String date;
}