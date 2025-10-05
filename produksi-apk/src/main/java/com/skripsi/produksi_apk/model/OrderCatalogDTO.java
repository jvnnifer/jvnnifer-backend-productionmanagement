package com.skripsi.produksi_apk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderCatalogDTO {
    @JsonProperty("catalog_id")
    private String catalogId;
    private Integer qty;

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
   
    
}

