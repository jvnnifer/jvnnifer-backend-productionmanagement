package com.skripsi.produksi_apk.model;

import com.skripsi.produksi_apk.entity.PreparationOrder;

public class PrepOrderApprovalResult {
    private boolean success;
    private String message;
    private PreparationOrder preparationOrder; 

    public PrepOrderApprovalResult(boolean success, String message, PreparationOrder preparationOrder) {
        this.success = success;
        this.message = message;
        this.preparationOrder = preparationOrder;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public PreparationOrder getPreparationOrder() { return preparationOrder; }
}