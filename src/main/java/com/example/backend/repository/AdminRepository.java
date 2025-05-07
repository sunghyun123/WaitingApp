package com.example.backend.repository;


import com.example.backend.entity.Admin;
import com.example.backend.entity.WaitingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {

    Admin findByUsernameAndPassword(String username, String password);
}
