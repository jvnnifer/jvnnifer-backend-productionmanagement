package com.skripsi.produksi_apk.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skripsi.produksi_apk.entity.CatalogItem;
import com.skripsi.produksi_apk.entity.Material;
import com.skripsi.produksi_apk.entity.Role;
import com.skripsi.produksi_apk.entity.User;
import com.skripsi.produksi_apk.service.ProductionService;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ProductionController {

    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    // Register
    @PostMapping("/user/register")
    public User register(@RequestParam String username, @RequestParam String password, @RequestParam String roleId) {
        return productionService.registerUser(username, password, roleId);
    }

    // Login
    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        Map<String, Object> userMap = productionService.login(loginData);
        if (userMap != null) {
            return ResponseEntity.ok(userMap);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    

    @GetMapping("/getuser/{id}")
    public User getUser(@PathVariable String id) {
        return productionService.getUser(id);
    }

    @PutMapping("/updateuser/{id}")
    public User updateUser(
            @PathVariable String id,
            @RequestParam String username, @RequestParam String password, @RequestParam String roleId) {
        return productionService.updateUser(id, username, password, roleId);
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return productionService.getAllRoles();
    }

    // ============= MATERIAL ==================
    @PostMapping("/material")
    public Material insertMaterial(@RequestBody Material material) {
        return productionService.insertMaterial(material);
    }

    @PostMapping("/update-material/{id}")
    public Material updateMaterial(@PathVariable String id, @RequestBody Material material) {
        return productionService.updateMaterial(id, material);
    }

    @GetMapping("/get-materials")
    public List<Material> getMaterial() {
        return productionService.getAllMaterials();
    }

    @PostMapping("/delete-material/{id}")
    public String deleteMaterial(@PathVariable String id) {
        productionService.deleteMaterial(id);
        return "Success Delete Material";

    }

    // ============== CATALOG ITEM ===============
    @PostMapping("/catalog")
    public CatalogItem insertCatalogItem(@RequestBody CatalogItem catalogItem) {
        return productionService.insertCatalogItem(catalogItem);
    }

    @PostMapping("/update-catalog/{id}")
    public CatalogItem updateCatalogItem(@PathVariable String id, @RequestBody CatalogItem catalogItem) {
        return productionService.updateCatalogItem(id, catalogItem);
    }

    @GetMapping("/get-catalog")
    public List<CatalogItem> getCatalogItems() {
        return productionService.getAllCatalogItem();
    }

    @PostMapping("/delete-catalog/{id}")
    public String deleteCatalog(@PathVariable String id) {
        productionService.deleteCatalogItem(id);
        return "Success Delete Catalog";

    }
}
