package com.skripsi.produksi_apk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skripsi.produksi_apk.entity.Material;

public interface MaterialRepository extends JpaRepository<Material, String> {
    @Query("SELECT m FROM Material m WHERE m.isDelete = false")
    List<Material> findAllActive();
}
