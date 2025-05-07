package com.example.backend.controller;

import com.example.backend.entity.Admin;
import com.example.backend.entity.Customer;
import com.example.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 1. 로그인 기능
    @PostMapping("/login")
    public Admin login(@RequestParam String username, @RequestParam String password) {
        return adminService.login(username, password);
    }

    // 2. 관리자 저장 기능
    @PostMapping("/save")
    public Admin saveAdmin(@RequestBody Admin admin) {
        return adminService.saveAdmin(admin);
    }

    //3213213213

}