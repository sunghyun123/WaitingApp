package com.example.backend.controller;

import com.example.backend.service.WaitingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/waiting")
public class WaitingController {

    private final WaitingTimeService waitingTimeService;

    @Autowired
    public WaitingController(WaitingTimeService waitingTimeService) {
        this.waitingTimeService = waitingTimeService;
    }

    @PostMapping("/estimate")
    public ResponseEntity<Map<String, Long>> getEstimatedWaitTime(@RequestBody Map<String, Long> body) {
        long currentClientWait = body.getOrDefault("currentWait", 0L);
        long waitTime = waitingTimeService.estimateWaitTime(currentClientWait);
        Map<String, Long> result = new HashMap<>();
        result.put("waitTime", waitTime);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/delete")
    public ResponseEntity<Map<String, Long>> deleteWaitTime(@RequestBody Map<String, Long> body) {
        long currentClientWait = body.getOrDefault("currentWait", 0L);
        long waitTime = waitingTimeService.deleteWaitTime(currentClientWait);
        Map<String, Long> result = new HashMap<>();
        result.put("waitTime", waitTime);
        return ResponseEntity.ok(result);
    }
}