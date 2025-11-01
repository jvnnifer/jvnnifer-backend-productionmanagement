package com.skripsi.produksi_apk.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.skripsi.produksi_apk.entity.OrderCatalog;
import com.skripsi.produksi_apk.model.CatalogItemOrderDTO;

import java.util.List;

public interface OrderCatalogRepository extends CrudRepository<OrderCatalog, Long> {

    @Query("""
        SELECT new com.skripsi.produksi_apk.model.CatalogItemOrderDTO(c.id, c.title, oc.qty)
        FROM OrderCatalog oc
        JOIN oc.catalogItem c
        WHERE oc.order.orderNo = :orderNo
    """)
    List<CatalogItemOrderDTO> findCatalogItemsByOrderNo(@Param("orderNo") String orderNo);

    List<OrderCatalog> findByOrder_OrderNo(String orderNo);
}
