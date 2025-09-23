package com.skripsi.produksi_apk.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.skripsi.produksi_apk.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
    
}

