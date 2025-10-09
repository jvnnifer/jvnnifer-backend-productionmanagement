package com.skripsi.produksi_apk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skripsi.produksi_apk.entity.MaterialLog;

public interface MaterialLogRepository extends JpaRepository<MaterialLog, Long> {
    @Query("SELECT COALESCE(SUM(m.qty), 0) FROM MaterialLog m WHERE m.type = :type")
    int sumQtyByType(@Param("type") String type);
}
