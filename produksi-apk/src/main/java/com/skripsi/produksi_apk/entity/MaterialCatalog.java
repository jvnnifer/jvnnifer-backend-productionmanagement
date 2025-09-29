package com.skripsi.produksi_apk.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "material_catalog")
public class MaterialCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "catalog_id", nullable = false)
    @JsonIgnore
    private CatalogItem catalog;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    @JsonIgnore
    private Material material;

    @Column(name = "req_qty", nullable = false)
    private Integer reqQty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogItem getCatalog() {
        return catalog;
    }

    public void setCatalog(CatalogItem catalog) {
        this.catalog = catalog;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Integer getReqQty() {
        return reqQty;
    }

    public void setReqQty(Integer reqQty) {
        this.reqQty = reqQty;
    }

    
}
