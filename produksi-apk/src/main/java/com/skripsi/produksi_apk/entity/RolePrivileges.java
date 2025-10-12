package com.skripsi.produksi_apk.entity;

import org.hibernate.annotations.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "view_role_privileges")
public class RolePrivileges {

    @EmbeddedId
    private RolePrivilegesPK pk;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "privilege_code")
    private String privilegeCode;

    @Column(name = "privilege_name")
    private String privilegeName;

    // Getters & Setters
    

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

   
    public String getPrivilegeCode() {
        return privilegeCode;
    }

    public void setPrivilegeCode(String privilegeCode) {
        this.privilegeCode = privilegeCode;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public RolePrivilegesPK getPk() {
        return pk;
    }

    public void setPk(RolePrivilegesPK pk) {
        this.pk = pk;
    }
}
