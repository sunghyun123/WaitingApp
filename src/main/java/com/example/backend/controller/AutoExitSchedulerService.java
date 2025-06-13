package com.example.backend.controller;

import com.example.backend.entity.Customer;
import com.example.backend.entity.SettingInfo;
import com.example.backend.repository.SettingInfoRepository;
import com.example.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AutoExitSchedulerService {

    private final CustomerService customerService; // CustomerService 주입
    private final SettingInfoRepository settingInfoRepository;

    @Autowired
    public AutoExitSchedulerService(CustomerService customerService,
                                    SettingInfoRepository settingInfoRepository) {
        this.customerService = customerService;
        this.settingInfoRepository = settingInfoRepository;
    }

    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    public void processAutoCustomerExit() {
        System.out.println("--- [스케줄러 실행: 자동 퇴장 처리] ---");
        LocalDateTime now = LocalDateTime.now();
        SettingInfo setting = settingInfoRepository.findTopByOrderByIdDesc();

        if (setting == null) {
            System.out.println("설정 정보가 없습니다. 자동 퇴장 처리를 건너뜝니다.");
            return;
        }

        // CustomerService에서 현재 입장 상태인 고객 목록을 가져옴
        List<Customer> enteredCustomers = customerService.getEnteredCustomers();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Customer customer : enteredCustomers) {
            try {
                LocalTime enteredLocalTime = LocalTime.parse(customer.getEnteredTime(), timeFormatter);
                LocalDate enteredDate = LocalDate.parse(customer.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDateTime enteredDateTime = LocalDateTime.of(enteredDate, enteredLocalTime);

                LocalDateTime expectedExitTime = enteredDateTime.plusMinutes(setting.getAverageWaitTime());

                if (now.isAfter(expectedExitTime)) {
                    System.out.println("고객 " + customer.getPhoneNumber() + " (" + customer.getId() + ")의 예상 퇴장 시간이 지났습니다. 퇴장 처리합니다.");
                    // CustomerService의 updateCustomerStatus 메서드 호출
                    customerService.updateCustomerStatus(customer.getId(), "exited");
                }
            } catch (Exception e) {
                System.err.println("고객 " + customer.getId() + " 퇴장 처리 중 오류 발생: " + e.getMessage());
            }
        }
        System.out.println("--- [스케줄러 종료: 자동 퇴장 처리] ---\n");
    }
}