package com.skripsi.produksi_apk.entity;

import java.util.List;

import javax.xml.catalog.Catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="material")
public class Material {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="stock_qty")
    private int stockQty;

    @Column(name="unit")
    private String unit;

    @ManyToMany(mappedBy = "materials")
    private List<Catalog> catalogs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }
}
