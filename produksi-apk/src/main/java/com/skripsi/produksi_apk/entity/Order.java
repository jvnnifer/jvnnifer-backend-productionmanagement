package com.skripsi.produksi_apk.entity;

import java.util.Date;
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
@Table(name="order")
public class Order {
    @Id
    @Column(name="order_no")
    private String orderNo;

    @Column(name="dept_store")
    private String deptStore;

    @Column(name="deadline")
    private Date deadline;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OrderCatalog> orderCatalogs = new ArrayList<>();

    @Column(name="status")
    private String status;

    @Column(name="notes")
    private String notes;
    
    @Lob
    @Column(name = "attachment", nullable = true)
    @JdbcType(org.hibernate.type.descriptor.jdbc.BinaryJdbcType.class)
    private byte[] attachment;

    
    public String getDeptStore() {
        return deptStore;
    }

    public void setDeptStore(String deptStore) {
        this.deptStore = deptStore;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public List<OrderCatalog> getOrderCatalogs() {
        return orderCatalogs;
    }

    public void setOrderCatalogs(List<OrderCatalog> orderCatalogs) {
        this.orderCatalogs = orderCatalogs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    
}
