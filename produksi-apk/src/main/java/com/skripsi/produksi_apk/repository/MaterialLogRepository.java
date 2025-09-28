package com.skripsi.produksi_apk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skripsi.produksi_apk.entity.MaterialLog;

public interface MaterialLogRepository extends JpaRepository<MaterialLog, Long> {
    
}
