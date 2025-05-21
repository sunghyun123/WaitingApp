package com.example.backend.repository;

import com.example.backend.entity.SettingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingInfoRepository extends JpaRepository<SettingInfo, Long> {
}