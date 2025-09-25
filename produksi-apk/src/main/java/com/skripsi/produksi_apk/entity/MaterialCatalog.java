package com.skripsi.produksi_apk.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "material_catalog")
public class MaterialCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "catalog_id", nullable = false)
    private CatalogItem catalog;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(name = "req_qty", nullable = false)
    private Integer reqQty;

    
}
