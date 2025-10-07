package com.skripsi.produksi_apk.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.skripsi.produksi_apk.entity.PreparationOrder;


public interface PreparationOrderRepository extends JpaRepository<PreparationOrder, String> {
    
}
