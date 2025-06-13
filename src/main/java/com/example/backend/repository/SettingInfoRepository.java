package com.example.backend.repository;

import com.example.backend.entity.SettingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingInfoRepository extends JpaRepository<SettingInfo, Long> {

    // 가장 최신 설정 한 개 가져오기 (id가 가장 큰)
    SettingInfo findTopByOrderByIdDesc();

    // 또는 업종(type) 기준으로 설정 가져오고 싶으면 이렇게
    SettingInfo findTopByTypeOrderByIdDesc(String type);
}