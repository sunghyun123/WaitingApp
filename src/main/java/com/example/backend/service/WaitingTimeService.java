package com.example.backend.service;

import com.example.backend.entity.Customer;
import com.example.backend.entity.SettingInfo;
import com.example.backend.repository.CustomerRepository;
import com.example.backend.repository.SettingInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.PriorityQueue;
import java.util.List;

@Service
public class WaitingTimeService {

    private final SettingInfoRepository settingInfoRepository;

    @Autowired
    public WaitingTimeService(SettingInfoRepository settingInfoRepository,
                              CustomerRepository customerRepository) {
        this.settingInfoRepository = settingInfoRepository;
    }

    public long estimateWaitTime(long clientRemainingTime) {
        SettingInfo setting = getLatestSetting();

        int avgWait = setting.getAverageWaitTime();

        return clientRemainingTime  + avgWait;
    }

    public long deleteWaitTime(long clientRemainingTime) {
        SettingInfo setting = getLatestSetting();

        int avgWait = setting.getAverageWaitTime();

        return clientRemainingTime  - avgWait;
    }

    private SettingInfo getLatestSetting() {
        return settingInfoRepository.findAll()
                .stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new IllegalStateException("설정 정보가 없습니다."));
    }
}
