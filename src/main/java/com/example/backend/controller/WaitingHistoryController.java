package com.example.backend.controller;


import com.example.backend.entity.WaitingHistory;
import com.example.backend.service.WaitingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/waiting-history")
public class WaitingHistoryController {

    private final WaitingHistoryService waitingHistoryService;


    @Autowired
    public WaitingHistoryController(WaitingHistoryService waitingHistoryService){
        this.waitingHistoryService = waitingHistoryService;
    }

    @GetMapping("/date-range")
    public List<WaitingHistory> getWaitingHistoryByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDate startOfDay = LocalDate.parse(startDate);
        LocalDate endOfDay = LocalDate.parse(endDate);

        return waitingHistoryService.getWaitingHistoryByDateRange(startOfDay,endOfDay);

    }

    @GetMapping("/customer/{phoneNumber}")
    public List<WaitingHistory> getWaitingHistoryByPhoneNumber(@PathVariable String phoneNumber) {
        return waitingHistoryService.getWaitingHistoryByPhoneNumber(phoneNumber);
    }

    @GetMapping("/completed")
    public List<WaitingHistory> getCompletedWaitingHistory(){
        return waitingHistoryService.getCompletedWaitingHistory();
    }

    @GetMapping("/pending")
    public List<WaitingHistory> getPendingWaitingHistory(){
        return waitingHistoryService.getPendingWaitingHistory();
    }

    @PostMapping("/add")
    public WaitingHistory addWaitingHistory(@RequestBody WaitingHistory waitingHistory) {
        return waitingHistoryService.addWaitingHistory(waitingHistory);
    }


    //예약 업데이트
    @PatchMapping("/update/{id}")
    public WaitingHistory updateWaitingHistory(@PathVariable Long id, @RequestBody WaitingHistory updatedWaitingHistory) {
        return waitingHistoryService.updateWaitingHistory(id, updatedWaitingHistory);
    }

    //예약 취소
    @DeleteMapping("/cancel/{id}")
    public void cancelWaitingHistory(@PathVariable Long id) {
        waitingHistoryService.cancelWaitingHistory(id);
    }

}
