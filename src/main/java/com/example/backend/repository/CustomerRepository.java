package com.example.backend.repository;

import com.example.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByPhoneNumber(String phoneNumber);

    // 가장 큰 대기번호 가진 고객
    Customer findTopByOrderByWaitingNumberDesc();

    // 전체 고객을 대기번호 순으로 정렬
    List<Customer> findAllByOrderByWaitingNumberAsc();
}