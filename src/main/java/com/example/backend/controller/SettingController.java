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
        settingService.saveSetting(settingRequest.getAverageWaitTime(), settingRequest.getTableCount());
        return ResponseEntity.ok("설정 저장 완료");
    }

    @GetMapping("/latest")
    public ResponseEntity<SettingInfo> getLatestSetting() {
        SettingInfo latest = settingService.getLatestSetting();
        return latest != null ? ResponseEntity.ok(latest) : ResponseEntity.noContent().build();
    }
}