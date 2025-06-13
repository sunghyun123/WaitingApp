package com.example.backend.service;

import com.example.backend.entity.SettingInfo;
import com.example.backend.repository.SettingInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    private final SettingInfoRepository settingInfoRepository;
    private final WaitingTimeService waitingTimeService;

    @Autowired
    public SettingService(SettingInfoRepository settingInfoRepository, WaitingTimeService waitingTimeService) {
        this.settingInfoRepository = settingInfoRepository;
        this.waitingTimeService = waitingTimeService;
    }

//    public SettingInfo saveSetting(int averageWaitTime, int tableCount) {
//        // 설정 정보는 새로 저장 (하나만 유지하고 싶다면 다른 방식 권장)
//        SettingInfo setting = new SettingInfo();
//        setting.setAverageWaitTime(averageWaitTime);
//        setting.setTableCount(tableCount);
//
//
//        return settingInfoRepository.save(setting);
//    }

    public void saveSetting(SettingInfo settingInfo) {
        settingInfoRepository.save(settingInfo);
    }

    public SettingInfo getLatestSetting() {
        List<SettingInfo> all = settingInfoRepository.findAll();
        return all.isEmpty() ? null : all.get(all.size() - 1);
    }
}
