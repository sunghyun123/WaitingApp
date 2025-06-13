package com.example.backend.controller;

import com.example.backend.entity.SettingInfo;
import com.example.backend.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setting")
public class SettingController {

    private final SettingService settingService;

    @Autowired
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @PostMapping("/set")
    public ResponseEntity<String> setWaitTime(@RequestBody SettingInfo settingRequest) {
        String type = settingRequest.getType();
        int averageWaitTime;

        switch (type) {
            case "한식":
                averageWaitTime = 37;
                break;
            case "중식":
                averageWaitTime = 34;
                break;
            case "일식":
                averageWaitTime = 32;
                break;
            case "양식":
                averageWaitTime = 28;
                break;
            case "아시안식":
                averageWaitTime = 15;
                break;
            default:
                return ResponseEntity.badRequest().body("지원하지 않는 음식 종류입니다.");
        }

        System.out.println(averageWaitTime);

        settingRequest.setAverageWaitTime(averageWaitTime);
        settingService.saveSetting(settingRequest);
        return ResponseEntity.ok("설정 저장 완료");
    }

    @GetMapping("/latest")
    public ResponseEntity<SettingInfo> getLatestSetting() {
        SettingInfo latest = settingService.getLatestSetting();
        return latest != null ? ResponseEntity.ok(latest) : ResponseEntity.noContent().build();
    }
}