package com.skripsi.produksi_apk.service;

import com.skripsi.produksi_apk.entity.CatalogItem;
import com.skripsi.produksi_apk.entity.Material;
import com.skripsi.produksi_apk.entity.Role;
import com.skripsi.produksi_apk.entity.User;
import com.skripsi.produksi_apk.model.LoginRequest;
import com.skripsi.produksi_apk.repository.CatalogItemRepository;
import com.skripsi.produksi_apk.repository.MaterialRepository;
import com.skripsi.produksi_apk.repository.RoleRepository;
import com.skripsi.produksi_apk.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@Service
public class ProductionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MaterialRepository materialRepository;
    private final CatalogItemRepository catalogItemRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ProductionService(UserRepository userRepository, RoleRepository roleRepository, MaterialRepository materialRepository, CatalogItemRepository catalogItemRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.materialRepository = materialRepository;
        this.catalogItemRepository = catalogItemRepository;
    }

    private String generateUserId() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('user_seq')", Long.class);
        return "USR" + String.format("%03d", nextVal);
    }

    private String generateMaterialId() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('material_seq')", Long.class);
        return "MAT" + String.format("%03d", nextVal);
    }

    public User registerUser(String username, String password, String roleId) {
        User user = new User();
        user.setId(generateUserId());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);
        return userRepository.save(user);
    }

    public Map<String, Object> login(Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", user.getId());
                result.put("username", user.getUsername());
                result.put("roleId", user.getRole().getId()); // asumsi User punya getRole()
                return result;
            }
        }
        return null;
    }



    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(String id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<Role> getAllRoles() {
        return roleRepository.findByIsOwnerNot(1);
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    // material
    public Material insertMaterial(Material material) {
        material.setId(generateMaterialId());
        material.setStockQty(material.getStockQty());
        material.setUnit(material.getUnit());
        material.setMaterialName(material.getMaterialName());
        return materialRepository.save(material);
    }

    public Material updateMaterial(String id, Material updatedMaterial) {
        return materialRepository.findById(id).map(material -> {
            material.setStockQty(updatedMaterial.getStockQty());
            material.setMaterialName(updatedMaterial.getMaterialName());
            material.setUnit(updatedMaterial.getUnit());
            return materialRepository.save(material);
        }).orElse(null);
    }

    public Material getMaterial(String id) {
        return materialRepository.findById(id).orElse(null);
    }

    public void deleteMaterial(String id) {
        Material material = materialRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Material not found"));

        for (CatalogItem catalog : material.getCatalogs()) {
            catalog.getMaterials().remove(material);
        }
        material.getCatalogs().clear();

        materialRepository.delete(material);
    }

}
