package com.example.backend.controller;

import com.example.backend.dto.PhoneNumberDTO;
import com.example.backend.entity.Customer;
import com.example.backend.service.CustomerService;
import com.example.backend.service.SmsService;
import com.example.backend.service.WaitingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private CustomerService customerService;

    private final WaitingTimeService waitingTimeService;

    public SmsController(WaitingTimeService waitingTimeService) {
        this.waitingTimeService = waitingTimeService;
    }


    // JSON 형식 요청 바디 받기
    @PostMapping("/send")
    public String sendSms(@RequestBody PhoneNumberDTO phoneNumberDTO) {

        String phoneNum = phoneNumberDTO.getPhoneNumber();

        Customer customer = customerService.getCustomerByPhoneNumber(phoneNum);
        String cleaned = phoneNumberDTO.getPhoneNumber().replaceAll("-", "");

        if (!isAllowed(cleaned)) {
            return "허용되지 않은 번호입니다.";
        }




        String message = "고객님의 웨이팅이 접수되었습니다.\n" +
                "인원: " + customer.getPeople() + "명\n" +
                "웨이팅 번호: " + customer.getWaitingNumber() + "번\n" +
                "예상 대기 시간: " + waitingTimeService.calculateExpectedWaitTimeForNewCustomer() + "분\n" +
                "변동 사항이 있을 경우에는 매장으로 연락 부탁 드립니다.";

        return smsService.sendMessage(cleaned, message);
    }

    private boolean isAllowed(String phone) {
        List<String> allowed = List.of(
                "01028582631", //장현우
                "01037415951", //조성현
                "01050517183", //강경성
                "01099675155", //신동훈
                "01026602854"  //안상현
                );
        return allowed.contains(phone);
    }
}