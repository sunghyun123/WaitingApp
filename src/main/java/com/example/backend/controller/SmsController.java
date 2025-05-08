package com.example.backend.controller;

import com.example.backend.dto.SmsRequestDto;
import com.example.backend.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    // JSON 형식 요청 바디 받기
    @PostMapping("/send")
    public String sendSms(@RequestBody SmsRequestDto smsRequest) {
        return smsService.sendMessage(smsRequest.getTo(), smsRequest.getMessage());
    }
}