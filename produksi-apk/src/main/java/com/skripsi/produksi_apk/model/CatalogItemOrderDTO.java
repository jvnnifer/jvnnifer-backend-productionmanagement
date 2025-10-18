package com.skripsi.produksi_apk.model;

public class CatalogItemOrderDTO {
    private String catalogId;
    private String title;
    private Integer qty;

    public CatalogItemOrderDTO(String catalogId, String title, Integer qty) {
        this.catalogId = catalogId;
        this.title = title;
        this.qty = qty;
    }

    
    public String getTitle() { return title; }
    public Integer getQty() { return qty; }
    public String getCatalogId() {return catalogId;}

}
