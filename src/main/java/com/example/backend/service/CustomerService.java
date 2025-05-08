package com.example.backend.service;

import com.example.backend.entity.Customer;
import com.example.backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 고객 저장
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // 전화번호로 고객 조회
    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    // 전체 고객을 대기순으로 정렬해서 조회
    public List<Customer> getAllCustomersSortedByWaitingNumber() {
        return customerRepository.findAllByOrderByWaitingNumberAsc();
    }

    // 가장 마지막 순서 고객의 대기번호 반환
    public int getMaxWaitingNumber() {
        Customer lastCustomer = customerRepository.findTopByOrderByWaitingNumberDesc();
        return (lastCustomer != null) ? lastCustomer.getWaitingNumber() : 0;
    }

    // 가장 앞순서(대기번호가 가장 작은) 고객 조회
    public Customer getFirstInLine() {
        List<Customer> sortedList = customerRepository.findAllByOrderByWaitingNumberAsc();
        return sortedList.isEmpty() ? null : sortedList.get(0);
    }

    // 고객 삭제
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }

    // 특정 대기번호 이후의 고객들의 대기번호를 1씩 줄임
    public void updateWaitingNumbersAfterDeletion(int deletedWaitingNumber) {
        List<Customer> customers = customerRepository.findAllByOrderByWaitingNumberAsc();
        for (Customer c : customers) {
            if (c.getWaitingNumber() > deletedWaitingNumber) {
                c.setWaitingNumber(c.getWaitingNumber() - 1);
                customerRepository.save(c);
            }
        }
    }
}
