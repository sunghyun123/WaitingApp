package com.example.backend.controller;

import com.example.backend.entity.Customer;
import com.example.backend.repository.CustomerRepository;
import com.example.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;


    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    /// 1. 전체 예약 목록 반환
    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomersSortedByWaitingNumber();
    }

    /// 2. 예약 (대기열 번호 부여)
    @PostMapping("/save")
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    /// 3. 입장
    @PostMapping("/enter/{phoneNumber}")
    public void enterCustomer(@PathVariable String phoneNumber) {
        customerService.changeCustomer(phoneNumber,"entered"); //현재 상태 waiting -> entered로 변경
    }


    /// 4. 삭제
    @PostMapping("/cancel/{phoneNumber}")
    public void cancelCustomer(@PathVariable String phoneNumber) {
        customerService.changeCustomer(phoneNumber, "canceled"); //현재 상태 waiting -> delete 로 변경
    }

    /// 5. 호출
    @PostMapping("/call/{phoneNumber}")
    public void callCustomer(@PathVariable String phoneNumber) {
        customerService.changeCustomer(phoneNumber, "canceled"); //현재 상태 waiting -> delete 로 변경
    }

    /// 6. 재등록
    @PostMapping("/re_save/{phoneNumber}")
    public void re_SaveCustomer(@PathVariable String phoneNumber) {
        customerService.changeCustomer(phoneNumber, "waiting"); //현재 상태 waiting -> delete 로 변경
    }




    /// DB에서 고객 삭제 처리 (테스트용)
    @DeleteMapping("/delete/{phoneNumber}")
    public String deleteCustomer(@PathVariable String phoneNumber) {
        System.out.println(phoneNumber);
        Customer customer = customerService.getCustomerByPhoneNumber(phoneNumber);
        if (customer == null) {
            return "해당 고객이 존재하지 않습니다.";
        }

        int deletedWaitingNumber = customer.getWaitingNumber();
        customerService.deleteCustomer(customer);
        customerService.updateWaitingNumbersAfterDeletion(deletedWaitingNumber);

        return "고객 삭제 완료: " + phoneNumber;
    }

    /// 고객 정보 조회 (휴대폰 번호로)
    @GetMapping("/{phoneNumber}")
    public Customer getCustomer(@PathVariable String phoneNumber) {
        return customerService.getCustomerByPhoneNumber(phoneNumber);
    }

    /// 현재 대기 팀 수 조회
    @GetMapping("/waiting-count")
    public ResponseEntity<Integer> getWaitingCount() {
        int waitingCount = customerService.getTodayWaitingCount();
        return ResponseEntity.ok(waitingCount);
    }



/// /////////////////////////////////////////////////////////////
    @GetMapping("/people-count/{date}")
    public ResponseEntity<Map<String, Object>> getPeopleCount(@PathVariable String date) {
        int totalPeople = customerService.getPeopleCountByDate(date);

        Map<String, Object> response = new HashMap<>();
        response.put("date", date);
        response.put("totalPeople", totalPeople);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/visit-stats-graph")
    public ResponseEntity<List<Map<String, Object>>> getVisitStatsGraph() {
        List<Map<String, Object>> stats = customerService.getVisitStatsGraph();
        return ResponseEntity.ok(stats);
    }


}
