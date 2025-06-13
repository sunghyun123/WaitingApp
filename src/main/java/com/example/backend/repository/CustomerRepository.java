package com.example.backend.repository;

import com.example.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByPhoneNumber(String phoneNumber);

    // 가장 큰 대기번호 가진 고객
    Customer findTopByOrderByWaitingNumberDesc();

    // 전체 고객을 대기번호 순으로 정렬
    List<Customer> findAllByOrderByWaitingNumberAsc();

    //날짜를 입력받으면 자동으로 증가되는 변수
    int countByDate(String date);

    int countByStatusAndDate(String status, String date);

    int countByDateAndStatus(String date, String status);

    Customer findFirstByStatusAndDateOrderByIdAsc(String status, String date);



    @Query("SELECT SUM(c.people) FROM Customer c WHERE c.date = :date")
    Integer sumPeopleByDate(@Param("date") String date);

    int countByStatus(String status);

    List<Customer> findByStatus(String status);
}