package com.example.backend.service;

import com.example.backend.entity.WaitingHistory;
import com.example.backend.repository.WaitingHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class WaitingHistoryService {

    private final WaitingHistoryRepository waitingHistoryRepository;

    @Autowired
    public WaitingHistoryService(WaitingHistoryRepository waitingHistoryRepository){
        this.waitingHistoryRepository = waitingHistoryRepository;

    }

    //날짜로 웨이팅 기록 조회
    public List<WaitingHistory> getWaitingHistoryByDateRange(LocalDate startOfDay, LocalDate endOfDay){
        return waitingHistoryRepository.findByCreatedAtBetween(startOfDay,endOfDay);
    }

    //핸드폰 번호로 웨이팅 기록 조회
    public List<WaitingHistory> getWaitingHistoryByPhoneNumber(String phoneNumber){
        return waitingHistoryRepository.findByCustomerPhoneNumber(phoneNumber);
    }


    //완료된 웨이팅 기록 조회
    public List<WaitingHistory> getCompletedWaitingHistory(){
        return waitingHistoryRepository.findByIsCompletedTrue();
    }

    //진행중인 웨이팅 리스트 조회
    public List<WaitingHistory> getPendingWaitingHistory(){
        return waitingHistoryRepository.findByIsCompletedFalse();
    }

    //웨이팅 리스트 추가
    public WaitingHistory addWaitingHistory(WaitingHistory waitingHistory) {
        return waitingHistoryRepository.save(waitingHistory);
    }


    //웨이팅 시간 설정
    public WaitingHistory updateWaitingHistory(Long id, WaitingHistory updatedWaitingHistory) {
        WaitingHistory existingHistory = waitingHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("웨이팅 기록을 찾을 수 없습니다."));

        existingHistory.setExpectedWaitingTime(updatedWaitingHistory.getExpectedWaitingTime());
        existingHistory.setIsCompleted(updatedWaitingHistory.getIsCompleted());
        // 다른 상태 업데이트 필요시 추가

        return waitingHistoryRepository.save(existingHistory);
    }


    //웨이팅 취소
    public void cancelWaitingHistory(Long id) {
        WaitingHistory existingHistory = waitingHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("웨이팅 기록을 찾을 수 없습니다."));

        waitingHistoryRepository.delete(existingHistory);
    }

}
