package com.skripsi.produksi_apk.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skripsi.produksi_apk.entity.PreparationOrder;


public interface PreparationOrderRepository extends JpaRepository<PreparationOrder, String> {
    @Query("SELECT COUNT(p) FROM PreparationOrder p")
    int sumTotalProduksi();

    @Query(value = "SELECT EXTRACT(MONTH FROM created_date) AS month, COUNT(*) " +
               "FROM preparation_order " +
               "WHERE EXTRACT(YEAR FROM created_date) = :year " +
               "GROUP BY EXTRACT(MONTH FROM created_date) " +
               "ORDER BY month", nativeQuery = true)
    List<Object[]> countPerMonth(@Param("year") int year);

    @Query(value = "SELECT EXTRACT(YEAR FROM created_date) AS year, COUNT(*) " +
               "FROM preparation_order " +
               "WHERE EXTRACT(YEAR FROM created_date) >= :startYear " +
               "GROUP BY EXTRACT(YEAR FROM created_date) " +
               "ORDER BY year", nativeQuery = true)
    List<Object[]> countPerYear(@Param("startYear") int startYear);
}
