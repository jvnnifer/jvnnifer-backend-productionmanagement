package com.skripsi.produksi_apk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skripsi.produksi_apk.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
}
