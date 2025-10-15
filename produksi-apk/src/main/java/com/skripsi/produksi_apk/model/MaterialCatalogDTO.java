package com.skripsi.produksi_apk.model;

public class MaterialCatalogDTO {
    private String materialId;
    private String materialName;
    private String unit;
    private Integer reqQty;

    public MaterialCatalogDTO() {
    }

    public MaterialCatalogDTO(String materialId, String materialName, String unit, Integer reqQty) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.unit = unit;
        this.reqQty = reqQty;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getReqQty() {
        return reqQty;
    }

    public void setReqQty(Integer reqQty) {
        this.reqQty = reqQty;
    }
}
