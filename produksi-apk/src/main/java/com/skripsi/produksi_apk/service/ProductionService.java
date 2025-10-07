package com.skripsi.produksi_apk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skripsi.produksi_apk.entity.CatalogItem;
import com.skripsi.produksi_apk.entity.Material;
import com.skripsi.produksi_apk.entity.MaterialCatalog;
import com.skripsi.produksi_apk.entity.MaterialLog;
import com.skripsi.produksi_apk.entity.Orders;
import com.skripsi.produksi_apk.entity.OrderCatalog;
import com.skripsi.produksi_apk.entity.Role;
import com.skripsi.produksi_apk.entity.User;
import com.skripsi.produksi_apk.model.CatalogItemOrderDTO;
import com.skripsi.produksi_apk.model.MaterialCatalogDTO;
import com.skripsi.produksi_apk.model.OrderCatalogDTO;
import com.skripsi.produksi_apk.repository.*;

import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MaterialRepository materialRepository;
    private final MaterialLogRepository materialLogRepository;
    private final CatalogItemRepository catalogItemRepository;
    private final OrderRepository orderRepository;
    private OrderCatalogRepository orderCatalogRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ProductionService(UserRepository userRepository, RoleRepository roleRepository, 
    MaterialRepository materialRepository, CatalogItemRepository catalogItemRepository, 
    MaterialLogRepository materialLogRepository, OrderRepository orderRepository, OrderCatalogRepository orderCatalogRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.materialRepository = materialRepository;
        this.catalogItemRepository = catalogItemRepository;
        this.materialLogRepository = materialLogRepository;
        this.orderRepository = orderRepository;
        this.orderCatalogRepository = orderCatalogRepository;
    }

    private String generateUserId() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('user_seq')", Long.class);
        return "USR" + String.format("%03d", nextVal);
    }

    private String generateMaterialId() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('material_seq')", Long.class);
        return "MAT" + String.format("%03d", nextVal);
    }

    private String generateCatalogId() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('catalog_seq')", Long.class);
        return "CAT" + String.format("%03d", nextVal);
    }

    private String generateOrderId() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('order_seq')", Long.class);
        return "ODR" + String.format("%03d", nextVal);
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
                result.put("roleId", user.getRole().getId()); 
                return result;
            }
        }
        return null;
    }

    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getUserByRole(String roleId) {
        return userRepository.findByRole_Id(roleId);
    }

    public User updateUser(String id, String username, String password, String roleId) {
        User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(username);

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (roleId != null) {
            Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }

        return userRepository.save(user);
    }


    public List<Role> getAllRoles() {
        return roleRepository.findByIsOwnerNot(1);
    }

    // ============ MATERIAL ================
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

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public void deleteMaterial(String id) {
        Material material = materialRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Material not found"));
        material.getCatalogs().clear();

        materialRepository.delete(material);
    }

    // =========== CATALOG ITEM ===============
    public CatalogItem insertCatalogItem(
        String title,
        String createdBy,
        String description,
        Double price,
        String materialsJson,
        MultipartFile file
) throws IOException {

    CatalogItem catalogItem = new CatalogItem();
    catalogItem.setId(generateCatalogId());
    catalogItem.setTitle(title);
    catalogItem.setCreatedBy(createdBy);
    catalogItem.setDescription(description);
    catalogItem.setPrice(price);

    if (file != null && !file.isEmpty()) {
        catalogItem.setAttachment(file.getBytes());
    }

    ObjectMapper mapper = new ObjectMapper();
    List<MaterialCatalogDTO> materialsDto = Arrays.asList(
        mapper.readValue(materialsJson, MaterialCatalogDTO[].class)
    );

    List<MaterialCatalog> materialEntities = new ArrayList<>();
    for (MaterialCatalogDTO dto : materialsDto) {
        MaterialCatalog mc = new MaterialCatalog();
        mc.setCatalog(catalogItem);

        Material material = materialRepository.findById(dto.getMaterialId())
            .orElseThrow(() -> new RuntimeException("Material not found: " + dto.getMaterialId()));

        mc.setMaterial(material);
        mc.setReqQty(dto.getReqQty());

        materialEntities.add(mc);
    }
    catalogItem.setMaterialCatalogs(materialEntities);
    return catalogItemRepository.save(catalogItem);
}


    public CatalogItem updateCatalogItem(String id, CatalogItem updatedCatalogItem) {
        return catalogItemRepository.findById(id).map(catalog -> {
            catalog.setCreatedBy(updatedCatalogItem.getCreatedBy());
            catalog.setDescription(updatedCatalogItem.getDescription());
            catalog.setMaterialCatalogs(updatedCatalogItem.getMaterialCatalogs());
            catalog.setPrice(updatedCatalogItem.getPrice());
            catalog.setTitle(updatedCatalogItem.getTitle());
            catalog.setAttachment(updatedCatalogItem.getAttachment());
            return catalogItemRepository.save(catalog);
        }).orElse(null);
    }

    public List<CatalogItem> getAllCatalogItem() {
        return catalogItemRepository.findAll();
    }

    public void deleteCatalogItem(String id) {
        CatalogItem catalogItem = catalogItemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Catalog Item not found"));

        catalogItemRepository.delete(catalogItem);
    }

    // ================ MATERIAL LOG ===============
    public MaterialLog insertMaterialLog(MaterialLog materialLog) {
        if (materialLog.getMaterial() != null && materialLog.getMaterial().getId() != null) {
            Material orderCatalog = materialRepository.findById(materialLog.getMaterial().getId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));
            materialLog.setMaterial(orderCatalog);
        }
        materialLog.setCreatedBy(materialLog.getCreatedBy());
        materialLog.setNote(materialLog.getNote());
        materialLog.setQty(materialLog.getQty());
        materialLog.setType(materialLog.getType());
        materialLog.setCreatedDate(materialLog.getCreatedDate());

        return materialLogRepository.save(materialLog);
    }

    public MaterialLog updateMaterialLog(Long id, MaterialLog updatedMaterialLog) {
        return materialLogRepository.findById(id).map(materialLog -> {
            materialLog.setId(id);
            materialLog.setNote(updatedMaterialLog.getNote());
            materialLog.setMaterial(updatedMaterialLog.getMaterial());
            materialLog.setType(updatedMaterialLog.getType());
            return materialLogRepository.save(materialLog);
        }).orElse(null);
    }

    public MaterialLog getMaterialLog(Long id) {
        return materialLogRepository.findById(id).orElse(null);
    }

    public List<MaterialLog> getAllMaterialLogs() {
        return materialLogRepository.findAll();
    }

    public void deleteMaterialLog(Long id) {
        MaterialLog materialLog = materialLogRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Material Log not found"));

        materialLogRepository.delete(materialLog);
    }

    // ====================== ORDER ============================
    public Orders insertOrder(
        String orderNo,
        String deptStore,
        Date deadline,
        String status,
        String notes,
        String orderCatalogsJson,
        MultipartFile file
    ) throws IOException {

        Orders order = new Orders();
        order.setOrderNo(orderNo);  
        order.setDeptStore(deptStore);
        order.setDeadline(deadline);
        order.setStatus(status);
        order.setNotes(notes);

        if (file != null && !file.isEmpty()) {
            order.setAttachment(file.getBytes());
        }

        ObjectMapper mapper = new ObjectMapper();
        List<OrderCatalogDTO> catalogsDto = Arrays.asList(
            mapper.readValue(orderCatalogsJson, OrderCatalogDTO[].class)
        );

        List<OrderCatalog> orderCatalogEntities = new ArrayList<>();
        for (OrderCatalogDTO dto : catalogsDto) {
            OrderCatalog oc = new OrderCatalog();
            oc.setOrder(order);

            CatalogItem catalogItem = catalogItemRepository.findById(dto.getCatalogId())
                .orElseThrow(() -> new RuntimeException("Catalog not found: " + dto.getCatalogId()));

            oc.setCatalogItem(catalogItem);
            oc.setQty(dto.getQty());


            orderCatalogEntities.add(oc);
        }

        order.setOrderCatalogs(orderCatalogEntities);

        return orderRepository.save(order);
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Orders> getOrderById(String orderNo) {
        return orderRepository.findById(orderNo);
    } 

    public Map<String, Object> getOrderWithCatalogItems(String orderNo) {
        Orders order = orderRepository.findById(orderNo)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<CatalogItemOrderDTO> catalogItems = orderCatalogRepository.findCatalogItemsByOrderNo(orderNo);

        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", order.getOrderNo());
        result.put("deptStore", order.getDeptStore());
        result.put("deadline", order.getDeadline());
        result.put("notes", order.getNotes());
        result.put("status", order.getStatus());
        result.put("attachment", order.getAttachment());
        result.put("catalogItems", catalogItems);

        return result;
    }
    
}
