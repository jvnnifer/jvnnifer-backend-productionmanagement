package com.skripsi.produksi_apk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skripsi.produksi_apk.entity.CatalogItem;
import com.skripsi.produksi_apk.entity.MaterialCatalog;

public interface MaterialCatalogRepository  extends JpaRepository<MaterialCatalog, Long>{
    
}
