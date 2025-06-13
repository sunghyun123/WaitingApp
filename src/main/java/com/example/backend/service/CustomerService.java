package com.example.backend.service;

import com.example.backend.entity.Customer;
import com.example.backend.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            customer.setEnteredTime("00:00");
        }
        else if (status.equals("waiting")) {
            customer.setEnteredTime("00:00");
            customer.setStatus(status);
            customer.setWaitingNumber(maxWaitingNumber+1);
            customerRepository.save(customer);
            return;
        } else if (status.equals("exited")) {
            customer.setStatus(status);
            customerRepository.save(customer);
        }

        customer.setStatus(status); //현재 상태 변경

        updateWaitingNumbersAfterDeletion(customer.getWaitingNumber()); //대기열 순위 조정
        customer.setWaitingNumber(0);

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
        customer.setWait(0);
        customer.setStatus("waiting");
        customer.setEnteredTime("00:00");

        return customerRepository.save(customer);
    }


    ///  즉시 입장
    public Customer directCustomer(Customer customer) {
        int maxWaitingNumber = getMaxWaitingNumber();
        LocalDateTime now = getCurrentTime();
        String todayDate = formatDate(now);

        int countToday = customerRepository.countByDate(todayDate);
        int waitingNumberDay = countToday + 1;

        customer.setStartTime(formatTime(now));
        customer.setDate(formatDate(now));
        customer.setWaitingNumber(0);
        customer.setWaitingNumber_day(waitingNumberDay);
        customer.setWait(0);
        customer.setStatus("entered");
        customer.setEnteredTime(formatTime(now));

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

    /// 날짜가 오늘이고, 현재 입장상태가 "entered"인 사람 중 가장 먼저 있는 컬럼 조회
    public Customer findFirstEnteredCustomerToday() {
        String today = LocalDate.now().toString(); // "yyyy-MM-dd" 형식
        return customerRepository.findFirstByStatusAndDateOrderByIdAsc("entered", today);
    }


    /// 현재 시간 구하기
    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }


    ///  현재 대기 인원 조회
    public int getTodayWaitingCount() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return customerRepository.countByStatusAndDate("waiting", today);
    }

    /// 현재 입장 인원 조회
    public int getTodayEnteredCount() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return customerRepository.countByStatusAndDate("entered", today);
    }

    // 웨이팅 시간 계산 및 업데이트
    public void updateWaitingTime(String phoneNumber) {
        Optional<Customer> optionalCustomer = Optional.ofNullable(customerRepository.findByPhoneNumber(phoneNumber));
        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("해당 고객이 존재하지 않습니다.");
        }

        Customer customer = optionalCustomer.get();

        // 시간 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start = LocalTime.parse(customer.getStartTime(), formatter);
        LocalTime end = LocalTime.parse(customer.getEnteredTime(), formatter);

        // 시간 차이 계산 (분 단위)
        long minutes = Duration.between(start, end).toMinutes();

        // 예외 처리: 음수 방지
        if (minutes < 0) {
            throw new IllegalStateException("입장 종료 시간이 시작 시간보다 빠릅니다.");
        }

        // 필드 업데이트 후 저장
        customer.setWait((int) minutes);
        customerRepository.save(customer);
    }

    private String formatTime(LocalDateTime time) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(timeFormatter);
    }

    private String formatDate(LocalDateTime time) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return time.format(dateFormatter);
    }


    public int getPeopleCountByDate(String date) {
        Integer result = customerRepository.sumPeopleByDate(date);
        return result != null ? result : 0;
    }

    public List<Map<String, Object>> getVisitStatsGraph() {
        List<Map<String, Object>> stats = new ArrayList<>();
        // 날짜 범위는 예시로 지난 7일을 가져옵니다. 필요에 따라 범위 조정 가능.
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // 각 날짜에 해당하는 방문자 수 가져오기
            int visitCount = customerRepository.countByDate(dateStr);

            Map<String, Object> stat = new HashMap<>();
            stat.put("date", dateStr);
            stat.put("count", visitCount);

            stats.add(stat);
        }

        return stats;
    }

    @Transactional // 트랜잭션 적용
    public void updateCustomerStatus(Long customerId, String newStatus) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setStatus(newStatus);
            customer.setWaitingNumber(0);
            customerRepository.save(customer);
            System.out.println("고객 ID: " + customerId + "의 상태가 '" + newStatus + "'(으)로 업데이트 되었습니다.");
        } else {
            System.out.println("고객 ID: " + customerId + "를 찾을 수 없습니다.");
        }
    }

    public List<Customer> getEnteredCustomers() {
        return customerRepository.findByStatus("entered");
    }



}
