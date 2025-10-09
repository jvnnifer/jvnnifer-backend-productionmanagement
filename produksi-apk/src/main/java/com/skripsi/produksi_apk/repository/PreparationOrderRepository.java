package com.skripsi.produksi_apk.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skripsi.produksi_apk.entity.PreparationOrder;


public interface PreparationOrderRepository extends JpaRepository<PreparationOrder, String> {
    @Query("SELECT COUNT(p) FROM PreparationOrder p")
    int sumTotalProduksi();
}
