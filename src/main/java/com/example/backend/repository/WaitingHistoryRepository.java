package com.example.backend.repository;


import com.example.backend.entity.WaitingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WaitingHistoryRepository extends JpaRepository<WaitingHistory, Long> {

    List<WaitingHistory> findByCreatedAtBetween(LocalDate startOfDay, LocalDate endOfDay);

    List<WaitingHistory> findByCustomerPhoneNumber(String phoneNumber);

    List<WaitingHistory> findByIsCompletedTrue();

    List<WaitingHistory> findByIsCompletedFalse();

}
