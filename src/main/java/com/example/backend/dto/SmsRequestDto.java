package com.example.backend.dto;

public class SmsRequestDto {
    private String to;
    private String message;

    // 기본 생성자 (필수)
    public SmsRequestDto() {}

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}