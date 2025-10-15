package com.skripsi.produksi_apk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skripsi.produksi_apk.entity.MaterialCatalog;

public interface MaterialCatalogRepository  extends JpaRepository<MaterialCatalog, Long>{
    List<MaterialCatalog> findByCatalog_Id(String catalogId);

}
