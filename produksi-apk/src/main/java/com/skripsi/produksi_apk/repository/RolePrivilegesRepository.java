package com.skripsi.produksi_apk.repository;
import com.skripsi.produksi_apk.entity.RolePrivileges;
import com.skripsi.produksi_apk.entity.RolePrivilegesPK;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePrivilegesRepository extends JpaRepository<RolePrivileges, RolePrivilegesPK> {
    List<RolePrivileges> findByPkRoleId(String roleId);
}
