package com.example.backend.controller;

import com.example.backend.entity.Customer;
import com.example.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    // 2. 고객 등록 또는 정보 수정
    @PostMapping("/save")
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/test/test")
    public void testDto() {
        System.out.println("testDto");
    }
}