package com.skripsi.produksi_apk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skripsi.produksi_apk.entity.CatalogItem;

public interface CatalogItemRepository extends JpaRepository<CatalogItem, String>{
    
}
