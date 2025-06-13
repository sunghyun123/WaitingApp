package com.example.backend.service;

import com.example.backend.entity.Customer;
import com.example.backend.entity.SettingInfo;
import com.example.backend.repository.CustomerRepository;
import com.example.backend.repository.SettingInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitingTimeService {

    private final SettingInfoRepository settingInfoRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public WaitingTimeService(SettingInfoRepository settingInfoRepository,
                              CustomerRepository customerRepository) {
        this.settingInfoRepository = settingInfoRepository;
        this.customerRepository = customerRepository;
    }

    public int calculateExpectedWaitTimeForNewCustomer() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        SettingInfo setting = settingInfoRepository.findTopByOrderByIdDesc();

        System.out.println("\n--- [계산 시작] ---");
        System.out.println("현재 시간: " + now);
        System.out.println("평균 이용 시간: " + setting.getAverageWaitTime() + "분");
        System.out.println("테이블 개수: " + setting.getTableCount());

        List<Customer> enteredCustomers = customerRepository.findByStatus("entered");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // 입장한 손님 중 최근 입장 시간 순서대로 정렬 후, 테이블 수만큼만 사용
        List<Customer> recentEntered = enteredCustomers.stream()
                .sorted((c1, c2) -> {
                    LocalTime t1 = LocalTime.parse(c1.getEnteredTime(), timeFormatter);
                    LocalTime t2 = LocalTime.parse(c2.getEnteredTime(), timeFormatter);
                    return t2.compareTo(t1); // 내림차순 (최신 순)
                })
                .limit(setting.getTableCount())
                .collect(Collectors.toList());

        List<LocalDateTime> tableFreeTimes = recentEntered.stream()
                .map(customer -> {
                    LocalTime startTime = LocalTime.parse(customer.getEnteredTime(), timeFormatter);
                    LocalDateTime startDateTime = LocalDateTime.of(today, startTime);


                    LocalDateTime freeTime = startDateTime.plusMinutes(setting.getAverageWaitTime());
                    System.out.println("손님 " + customer.getPhoneNumber() + "의 퇴장 예상 시간: " + freeTime);
                    return freeTime;
                })
                .collect(Collectors.toList());

        // 빈 테이블 수만큼 현재 시간으로 채우기
        int emptyTableCount = setting.getTableCount() - tableFreeTimes.size();
        for (int i = 0; i < emptyTableCount; i++) {
            tableFreeTimes.add(now);
            System.out.println("빈 테이블 추가: 현재 시간(" + now + ") 기준");
        }

        // PriorityQueue 생성
        PriorityQueue<LocalDateTime> pq = new PriorityQueue<>(tableFreeTimes);

        int waitingCount = customerRepository.countByStatus("waiting");
        int newCustomerIndex = waitingCount;
        System.out.println("대기 중인 손님 수: " + waitingCount);
        System.out.println("새 손님은 대기열에서 " + (newCustomerIndex + 1) + "번째 입니다.");

        LocalDateTime assignedTime = null;

        // 시뮬레이션: 손님들이 테이블을 순차적으로 사용
        for (int i = 0; i <= newCustomerIndex; i++) {
            LocalDateTime availableTime = pq.poll();
            System.out.println((i + 1) + "번째 손님에게 배정된 시간: " + availableTime);

            assignedTime = availableTime;
            LocalDateTime nextFreeTime = availableTime.plusMinutes(setting.getAverageWaitTime());
            pq.offer(nextFreeTime);

            System.out.println("해당 테이블은 다음 손님을 위해 " + nextFreeTime + "부터 다시 사용 가능");
        }

        long waitMinutes = java.time.Duration.between(now, assignedTime).toMinutes();
        int result = (int) Math.max(waitMinutes, 0);

        System.out.println("최종 배정 시간: " + assignedTime);
        System.out.println("예상 대기 시간: " + result + "분");
        System.out.println("--- [계산 종료] ---\n");

        return result;
    }
}
