package com.example.backend.service;

import com.example.backend.entity.Customer;
import com.example.backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;




    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }

    ///1. 예약 조회
    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    /// 2. 예약(신규 예약, 기존 예약 업데이트)
    public Customer saveCustomer(Customer customer) {
        Customer existingCustomer = getCustomerByPhoneNumber(customer.getPhoneNumber());

        if (existingCustomer != null) {
            updateCustomerInfo(existingCustomer, customer);
            return customerRepository.save(existingCustomer);
        }

        return registerNewCustomer(customer);
    }

    /// 3. 예약 상태 변경(입장, 삭제)
    public void changeCustomer(String phoneNumber, String status) {
        int maxWaitingNumber = getMaxWaitingNumber();
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
        LocalDateTime now = getCurrentTime();


        if (customer == null) return;

        if (status.equals("entered")) {
            customer.setEnteredTime(formatTime(now));
        } else if (status.equals("canceled")) {
            customer.setEnteredTime("canceled");
        }
        else if (status.equals("waiting")) {
            customer.setEnteredTime("미입장");
            customer.setStatus(status);
            customer.setWaitingNumber(maxWaitingNumber+1);
            customerRepository.save(customer);
            return;
        }

        customer.setStatus(status); //현재 상태 변경

        updateWaitingNumbersAfterDeletion(customer.getWaitingNumber()); //대기열 순위 조정
        customer.setWaitingNumber(-1);

        customerRepository.save(customer); //DB 반영
    }


    /// 4. DB 삭제
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }




    /// 신규 예약
    private Customer registerNewCustomer(Customer customer) {
        int maxWaitingNumber = getMaxWaitingNumber();
        LocalDateTime now = getCurrentTime();
        String todayDate = formatDate(now);

        int countToday = customerRepository.countByDate(todayDate);
        int waitingNumberDay = countToday + 1;

        customer.setStartTime(formatTime(now));
        customer.setDate(formatDate(now));
        customer.setWaitingNumber(maxWaitingNumber + 1);
        customer.setWaitingNumber_day(waitingNumberDay);
        customer.setWait(10);
        customer.setStatus("waiting");
        customer.setEnteredTime("미입장");

        return customerRepository.save(customer);
    }



    /// 기존 예약 업데이트
    private void updateCustomerInfo(Customer existingCustomer, Customer newCustomer) {
        existingCustomer.setWaitingNumber_day(newCustomer.getWaitingNumber_day());
        existingCustomer.setWaitingNumber(newCustomer.getWaitingNumber());
        existingCustomer.setStartTime(newCustomer.getStartTime());
        existingCustomer.setDate(newCustomer.getDate());
        existingCustomer.setWait(newCustomer.getWait());
        existingCustomer.setStatus(newCustomer.getStatus());
        existingCustomer.setEnteredTime(newCustomer.getEnteredTime());
    }




    /// 전체 고객을 대기순으로 정렬해서 조회
    public List<Customer> getAllCustomersSortedByWaitingNumber() {
        return customerRepository.findAllByOrderByWaitingNumberAsc();
    }

    /// 가장 마지막 순서 고객의 대기번호 반환
    public int getMaxWaitingNumber() {
        Customer lastCustomer = customerRepository.findTopByOrderByWaitingNumberDesc();
        return (lastCustomer != null) ? lastCustomer.getWaitingNumber() : 0;
    }

    /// 가장 앞순서(대기번호가 가장 작은) 고객 조회
    public Customer getFirstInLine() {
        List<Customer> sortedList = customerRepository.findAllByOrderByWaitingNumberAsc();
        return sortedList.isEmpty() ? null : sortedList.get(0);
    }


    /// 특정 대기번호 이후의 고객들의 대기번호를 1씩 줄임
    public void updateWaitingNumbersAfterDeletion(int deletedWaitingNumber) {
        List<Customer> customers = customerRepository.findAllByOrderByWaitingNumberAsc();
        for (Customer c : customers) {
            if (c.getWaitingNumber() > deletedWaitingNumber) {
                c.setWaitingNumber(c.getWaitingNumber() - 1);
                customerRepository.save(c);
            }
        }
    }


    /// 현재 시간 구하기
    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    private String formatTime(LocalDateTime time) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(timeFormatter);
    }

    private String formatDate(LocalDateTime time) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return time.format(dateFormatter);
    }



}
