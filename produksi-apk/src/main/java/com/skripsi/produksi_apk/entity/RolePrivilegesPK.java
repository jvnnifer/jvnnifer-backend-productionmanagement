package com.skripsi.produksi_apk.entity;

import java.io.Serializable;

import jakarta.persistence.Column;

public class RolePrivilegesPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name="role_id")
    private String roleId;

    @Column(name="privilege_id")
    private String privilege_id;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPrivilege_id() {
        return privilege_id;
    }

    public void setPrivilege_id(String privilege_id) {
        this.privilege_id = privilege_id;
    }

    
}
