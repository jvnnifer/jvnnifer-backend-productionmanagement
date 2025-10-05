package com.skripsi.produksi_apk.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skripsi.produksi_apk.entity.CatalogItem;
import com.skripsi.produksi_apk.entity.Material;
import com.skripsi.produksi_apk.entity.MaterialLog;
import com.skripsi.produksi_apk.entity.Orders;
import com.skripsi.produksi_apk.entity.Role;
import com.skripsi.produksi_apk.entity.User;
import com.skripsi.produksi_apk.service.ProductionService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


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
    @PostMapping(value = "/catalog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CatalogItem insertCatalogItem(
            @RequestParam("title") String title,
            @RequestParam("createdBy") String createdBy,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("materials") String materialsJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        return productionService.insertCatalogItem(title, createdBy, description, price, materialsJson, file);
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

    // =============== MATERIAL LOG ====================
    @PostMapping("/material-log")
    public MaterialLog insertMaterialLog(@RequestBody MaterialLog materialLog) {
        return productionService.insertMaterialLog(materialLog);
    }

    @PostMapping("/update-material-log/{id}")
    public MaterialLog updateMaterialLog(@PathVariable Long id, @RequestBody MaterialLog materialLog) {
        return productionService.updateMaterialLog(id, materialLog);
    }

    @GetMapping("/get-material-log")
    public List<MaterialLog> getMaterialLogs() {
        return productionService.getAllMaterialLogs();
    }

    @PostMapping("/delete-material-log/{id}")
    public String deleteMaterialLog(@PathVariable Long id) {
        productionService.deleteMaterialLog(id);
        return "Success Delete Material Log";
    }

    // ================== ORDER ===========================
    @PostMapping("/order")
    public Orders insertOrder(
            @RequestParam("orderNo") String orderNo,
            @RequestParam("deptStore") String deptStore,
            @RequestParam("deadline")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
            @RequestParam("status") String status,
            @RequestParam("orderCatalog") String orderCatalog,
            @RequestParam("notes") String notes,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        
        return productionService.insertOrder(orderNo, deptStore, deadline, status, notes, orderCatalog, file);
    }
    
    @GetMapping("/get-order")
    public List<Orders> getOrders() {
        return productionService.getAllOrders();
    }

    @GetMapping("/get-order-by-id")
    public Optional<Orders> getOrderById(@RequestParam String orderNo) {
        return productionService.getOrderById(orderNo);
    }
}
