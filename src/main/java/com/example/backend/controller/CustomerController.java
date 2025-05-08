package com.example.backend.controller;

import com.example.backend.entity.Customer;
import com.example.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // 1. 고객 정보 조회 (휴대폰 번호로)
    @GetMapping("/{phoneNumber}")
    public Customer getCustomer(@PathVariable String phoneNumber) {
        return customerService.getCustomerByPhoneNumber(phoneNumber);
    }

    // 2. 고객 등록 (대기열 번호 부여)
    @PostMapping("/save")
    public Customer saveCustomer(@RequestBody Customer customer) {
        Customer existingCustomer = customerService.getCustomerByPhoneNumber(customer.getPhoneNumber());

        if (existingCustomer != null) {
            return existingCustomer; // 이미 등록된 경우 기존 정보 반환
        }

        int maxWaitingNumber = customerService.getMaxWaitingNumber();
        customer.setWaitingNumber(maxWaitingNumber + 1);

        return customerService.saveCustomer(customer);
    }

    // 3. 고객 입장 처리 (가장 앞순서 고객 제거 + 전체 대기열 업데이트)
    @PostMapping("/enter")
    public String processEntry() {
        Customer firstCustomer = customerService.getFirstInLine();
        if (firstCustomer == null) {
            return "대기 중인 고객이 없습니다.";
        }

        customerService.deleteCustomer(firstCustomer);
        customerService.updateWaitingNumbersAfterDeletion(firstCustomer.getWaitingNumber());

        return "입장 처리된 고객 번호: " + firstCustomer.getPhoneNumber();
    }

    // 4. 고객 삭제 처리 (전화번호로 삭제)
    @DeleteMapping("/delete/{phoneNumber}")
    public String deleteCustomer(@PathVariable String phoneNumber) {
        Customer customer = customerService.getCustomerByPhoneNumber(phoneNumber);
        if (customer == null) {
            return "해당 고객이 존재하지 않습니다.";
        }

        int deletedWaitingNumber = customer.getWaitingNumber();
        customerService.deleteCustomer(customer);
        customerService.updateWaitingNumbersAfterDeletion(deletedWaitingNumber);

        return "고객 삭제 완료: " + phoneNumber;
    }

    // 전체 고객 대기열 목록 반환 (디버깅용)
    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomersSortedByWaitingNumber();
    }
}
