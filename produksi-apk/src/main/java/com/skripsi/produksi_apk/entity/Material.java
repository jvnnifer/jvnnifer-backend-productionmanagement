package com.skripsi.produksi_apk.entity;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="material")
public class Material {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="name")
    private String materialName;

    @Column(name="stock_qty")
    private int stockQty;

    @Column(name="unit")
    private String unit;

    @Column(name="is_delete")
    private Boolean isDelete = false;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MaterialCatalog> materialCatalogs = new ArrayList<>();

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

    public List<MaterialCatalog> getCatalogs() {
        return materialCatalogs;
    }

    public void setCatalogs(List<MaterialCatalog> materialCatalogs) {
        this.materialCatalogs = materialCatalogs;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
