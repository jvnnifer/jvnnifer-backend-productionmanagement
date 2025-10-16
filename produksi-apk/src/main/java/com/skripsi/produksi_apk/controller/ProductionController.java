package com.skripsi.produksi_apk.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
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
import com.skripsi.produksi_apk.entity.MaterialCatalog;
import com.skripsi.produksi_apk.entity.MaterialLog;
import com.skripsi.produksi_apk.entity.Orders;
import com.skripsi.produksi_apk.entity.PreparationOrder;
import com.skripsi.produksi_apk.entity.Role;
import com.skripsi.produksi_apk.entity.RolePrivileges;
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

    @GetMapping("/get-user-by-role/{roleId}")
    public List<User> getUserByRole(@PathVariable String roleId) {
        return productionService.getUserByRole(roleId);
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

    @GetMapping("/owner-role")
    public List<Role> getOwnerRole() {
        return productionService.getOwner();
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

    @PutMapping(value = "/update-catalog/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CatalogItem updateCatalogItem(
            @PathVariable String id,
            @RequestParam("title") String title,
            @RequestParam("createdBy") String createdBy,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("materials") String materialsJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        return productionService.updateCatalogItem(id, title, createdBy, description, price, materialsJson, file);
    }

    @GetMapping("/get-catalog")
    public List<CatalogItem> getCatalogItems() {
        return productionService.getAllCatalogItem();
    }

    @GetMapping("/get-materials-for-catalog/{id}")
    public ResponseEntity<Map<String, Object>> getMaterialsForCatalog(@PathVariable String id) {
        Map<String, Object> result = productionService.getMaterialsForCatalog(id);
        return ResponseEntity.ok(result);
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

    @GetMapping("/material-log/summary")
    public Map<String, Integer> getSummaryMaterialLog() {
        return productionService.getMaterialLogSummary();
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

    @PutMapping(value = "/update-order/{orderNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Orders updateOrder(
            @PathVariable String orderNo,
            @RequestParam("deptStore") String deptStore,
            @RequestParam("deadline") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
            @RequestParam("status") String status,
            @RequestParam("notes") String notes,
            @RequestParam("orderCatalog") String orderCatalogsJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        if(orderNo == null || orderNo.isEmpty()){
            throw new IllegalArgumentException("OrderNo must not be null");
        }
        System.out.println("OrderNo: " + orderNo);
        System.out.println("DeptStore: " + deptStore);
        System.out.println("Deadline: " + deadline);
        System.out.println("Status: " + status);
        System.out.println("Notes: " + notes);
        System.out.println("OrderCatalog: " + orderCatalogsJson);
        System.out.println("File: " + (file != null ? file.getOriginalFilename() : "null"));

        return productionService.updateOrder(orderNo, deptStore, deadline, status, notes, orderCatalogsJson, file);
    }
    
    @GetMapping("/get-order")
    public List<Orders> getOrders() {
        return productionService.getAllOrders();
    }

    @GetMapping("/get-order-by-id")
    public ResponseEntity<Map<String, Object>> getOrderById(@RequestParam String orderNo) {
        Map<String, Object> order = productionService.getOrderWithCatalogItems(orderNo);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/order/{orderNo}/attachment")
    public ResponseEntity<byte[]> getOrderAttachment(@PathVariable String orderNo) {
        byte[] attachment = productionService.getOrderAttachment(orderNo);

        if (attachment == null || attachment.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(attachment);
    }


    // =============== PREPARATION ORDER =================
    @PostMapping("/preparation-order")
    public ResponseEntity<?> insertPreparationOrder(@RequestBody PreparationOrder preparationOrder) {
        try {
            PreparationOrder saved = productionService.insertPreparationOrder(preparationOrder);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/update-preparation-order/{id}")
    public PreparationOrder updatePreparationOrder(@PathVariable String id, @RequestBody PreparationOrder preparationOrder) {
        return productionService.updatePreparationOrder(id, preparationOrder);
    }

    @GetMapping("/get-preparation-order")
    public List<PreparationOrder> getPreparationOrder() {
        return productionService.getAllPreparationOrders();
    }

    @PostMapping("/delete-preparation-order/{id}")
    public String deletePreparationOrder(@PathVariable String id) {
        productionService.deletePreparationOrder(id);
        return "Success Delete Preparation Order";

    }

    @PutMapping("/update-preporder-status/{id}")
    public ResponseEntity<PreparationOrder> updatePreparationOrderStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        
        String status = body.get("status");
        System.out.println("Received status: " + status); 
        PreparationOrder updated = productionService.updatePreparationOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }
    

    // ROLE & PRIVILEGES

    @GetMapping("/allprivileges")
    public List<RolePrivileges> getAllPrivileges() {
        return productionService.getAllRolePrivileges();
    }
    
    @GetMapping("/{roleId}/privileges")
    public List<RolePrivileges> getPrivilegesByRole(@PathVariable String roleId) {
        return productionService.getPrivilegesByRole(roleId);
    }

    @PostMapping("/update-privileges/{roleId}")
    public ResponseEntity<Role> updateRolePrivileges(
            @PathVariable String roleId,
            @RequestBody List<String> privilegeIds) {

        Role updatedRole = productionService.updateRolePrivileges(roleId, privilegeIds);
        return ResponseEntity.ok(updatedRole);
    }
    
    // DASHBOARD
    @GetMapping("/dashboard/preparation-order/monthly")
    public Map<Integer, Integer> getMonthly(@RequestParam int year) {
        return productionService.getPreparationOrderPerMonth(year);
    }

    @GetMapping("/dashboard/preparation-order/yearly")
    public Map<Integer, Integer> getYearly() {
        return productionService.getPreparationOrderPerYear();
    }

    @GetMapping("/dashboard/order/monthly")
    public Map<Integer, Integer> getOrderMonthly(@RequestParam int year) {
        return productionService.getOrderPerMonth(year);
    }

    @GetMapping("/dashboard/order/yearly")
    public Map<Integer, Integer> getOrderYearly() {
        return productionService.getOrderPerYear();
    }
}
