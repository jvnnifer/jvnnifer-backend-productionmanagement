package com.skripsi.produksi_apk.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="catalog_item")
public class CatalogItem {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="created_by")
    private String createdBy;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="price")
    private Double price;

    @Lob
    @Column(name = "attachment", nullable = true)
    @JdbcType(org.hibernate.type.descriptor.jdbc.BinaryJdbcType.class)
    private byte[] attachment;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore 
    private List<MaterialCatalog> materialCatalogs = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public List<MaterialCatalog> getMaterialCatalogs() {
        return materialCatalogs;
    }

    public void setMaterialCatalogs(List<MaterialCatalog> materialCatalogs) {
        this.materialCatalogs = materialCatalogs;
    }

    
}
