package com.skripsi.produksi_apk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skripsi.produksi_apk.entity.CatalogItem;

public interface CatalogItemRepository extends JpaRepository<CatalogItem, String>{
    @Query("SELECT c FROM CatalogItem c WHERE c.isDelete = false")
    List<CatalogItem> findAllActive();
}
