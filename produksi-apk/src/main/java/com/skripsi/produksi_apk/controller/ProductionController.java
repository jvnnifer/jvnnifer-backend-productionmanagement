package com.skripsi.produksi_apk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skripsi.produksi_apk.entity.User;
import com.skripsi.produksi_apk.service.ProductionService;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ProductionController {
    // @GetMapping("/test")
    // public String getMethodName(@RequestParam String param) {
    //     return new String("Test " + param);
    // }

    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    // Register
    @PostMapping("/user/register")
    public User register(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        return productionService.registerUser(username, password, role);
    }

    // Login
    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        String result = productionService.login(username, password);
        if (result.startsWith("Login success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) {
        return productionService.getUser(id);
    }

    @PutMapping("/user/{id}")
    public User updateUser(
            @PathVariable String id,
            @RequestBody User updatedUser) {
        return productionService.updateUser(id, updatedUser);
    }

}
