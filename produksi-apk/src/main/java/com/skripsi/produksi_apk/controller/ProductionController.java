package com.skripsi.produksi_apk.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skripsi.produksi_apk.entity.Material;
import com.skripsi.produksi_apk.entity.Role;
import com.skripsi.produksi_apk.entity.User;
import com.skripsi.produksi_apk.model.LoginRequest;
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
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String userId = productionService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login failed. Username or password incorrect.");
        }
    }
    

    @GetMapping("/getuser/{id}")
    public User getUser(@PathVariable String id) {
        return productionService.getUser(id);
    }

    @PutMapping("/updateuser/{id}")
    public User updateUser(
            @PathVariable String id,
            @RequestBody User updatedUser) {
        return productionService.updateUser(id, updatedUser);
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return productionService.getAllRoles();
    }

    // material
    @PostMapping("/material")
    public Material insertMaterial(@RequestParam String materialName, @RequestParam int stock_qty, @RequestParam String unit) {
        return productionService.insertMaterial(materialName, stock_qty, unit);
    }

    @PostMapping("/update-material/{id}")
    public Material updateMaterial(@PathVariable String id, @RequestBody Material material) {
        return productionService.updateMaterial(id, material);
    }

    @GetMapping("/get-materials/{id}")
    public Material getMaterial(@PathVariable String id) {
        return productionService.getMaterial(id);
    }

    @PostMapping("/delete-material/{id}")
    public String deleteMaterial(@PathVariable String id) {
        productionService.deleteMaterial(id);
        return "Success Delete Material";

    }
}
