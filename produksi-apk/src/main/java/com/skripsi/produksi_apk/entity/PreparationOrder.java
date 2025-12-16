package com.skripsi.produksi_apk.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="preparation_order")
public class PreparationOrder {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="status")
    private String status;

    @Column(name="note")
    private String note;

    @Column(name="production_pic")
    private String productionPic;

    @Column(name="approval_pic")
    private String approvalPic;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="start_date")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="end_date")
    private Date endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="actual_end_date")
    private Date actualEndDate;

    @PrePersist
    protected void onCreate() {
        createdDate = new Date(); 
    }

    
    // @JoinColumn(name = "order_id", referencedColumnName = "order_no")
    @OneToOne
    @JoinColumn(name="order_id")
    private Orders orders;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getProductionPic() {
        return productionPic;
    }

    public void setProductionPic(String productionPic) {
        this.productionPic = productionPic;
    }

    public String getApprovalPic() {
        return approvalPic;
    }

    public void setApprovalPic(String approvalPic) {
        this.approvalPic = approvalPic;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    
}
