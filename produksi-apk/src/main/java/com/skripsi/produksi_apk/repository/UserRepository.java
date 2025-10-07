package com.skripsi.produksi_apk.repository;

import com.skripsi.produksi_apk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    List<User> findByRole_Id(String roleId);
}

