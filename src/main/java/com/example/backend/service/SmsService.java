package com.example.backend.service;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import net.nurigo.sdk.NurigoApp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final DefaultMessageService messageService;

    public SmsService(
            @Value("${coolsms.api-key}") String apiKey,
            @Value("${coolsms.api-secret}") String apiSecret
    ) {
        // SDK 초기화 (API 키, 시크릿 키, 기본 URL)
        this.messageService = NurigoApp.INSTANCE.initialize(
                apiKey,
                apiSecret,
                "https://api.coolsms.co.kr"
        );
    }

    @Value("${coolsms.from}")
    private String from;

    public String sendMessage(String to, String text) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText(text);

        // ✅ 수정된 부분
        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        SingleMessageSentResponse response = messageService.sendOne(request);

        return response.toString();
    }
}
