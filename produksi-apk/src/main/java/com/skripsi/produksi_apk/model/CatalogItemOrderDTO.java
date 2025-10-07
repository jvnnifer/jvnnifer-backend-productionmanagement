package com.skripsi.produksi_apk.model;

public class CatalogItemOrderDTO {
    private String title;
    private Integer qty;

    public CatalogItemOrderDTO(String title, Integer qty) {
        this.title = title;
        this.qty = qty;
    }

    public String getTitle() { return title; }
    public Integer getQty() { return qty; }

}
