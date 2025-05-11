package com.example.backend.dto;



public class PhoneNumberDTO {
    private String phoneNumber;

    public PhoneNumberDTO() {} // 기본 생성자 꼭 필요함

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}