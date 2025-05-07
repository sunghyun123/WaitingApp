package com.example.backend.service;

import com.example.backend.entity.Admin;
import com.example.backend.entity.Customer;
import com.example.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    public Admin login(String username, String password){
        return adminRepository.findByUsernameAndPassword(username, password);
    }

    public Admin saveAdmin(Admin admin){
        return adminRepository.save(admin);
    }

}
