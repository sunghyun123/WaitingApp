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

    @GetMapping("/expected-wait-time")
    public ResponseEntity<Integer> getExpectedWaitTime() {
        int waitMinutes = waitingTimeService.calculateExpectedWaitTimeForNewCustomer();
        return ResponseEntity.ok(waitMinutes);
    }

}