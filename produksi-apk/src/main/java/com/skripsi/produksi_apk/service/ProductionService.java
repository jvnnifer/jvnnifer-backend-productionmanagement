package com.skripsi.produksi_apk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skripsi.produksi_apk.entity.CatalogItem;
import com.skripsi.produksi_apk.entity.Material;
import com.skripsi.produksi_apk.entity.MaterialCatalog;
import com.skripsi.produksi_apk.entity.MaterialLog;
import com.skripsi.produksi_apk.entity.Orders;
import com.skripsi.produksi_apk.entity.PreparationOrder;
import com.skripsi.produksi_apk.entity.Privileges;
import com.skripsi.produksi_apk.entity.OrderCatalog;
import com.skripsi.produksi_apk.entity.Role;
import com.skripsi.produksi_apk.entity.RolePrivileges;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.catalog.Catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductionService {

    private final MaterialCatalogRepository materialCatalogRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MaterialRepository materialRepository;
    private final MaterialLogRepository materialLogRepository;
    private final CatalogItemRepository catalogItemRepository;
    private final OrderRepository orderRepository;
    private final OrderCatalogRepository orderCatalogRepository;
    private final PreparationOrderRepository preparationOrderRepository;
    private final RolePrivilegesRepository rolePrivilegesRepository;
    private final PrivilegesRepository privilegesRepository;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ProductionService(UserRepository userRepository, 
    RoleRepository roleRepository, 
    MaterialRepository materialRepository, 
    CatalogItemRepository catalogItemRepository, 
    MaterialLogRepository materialLogRepository, 
    OrderRepository orderRepository, 
    OrderCatalogRepository orderCatalogRepository,
    PreparationOrderRepository preparationOrderRepository,
    RolePrivilegesRepository rolePrivilegesRepository,
    PrivilegesRepository privilegesRepository, 
    MaterialCatalogRepository materialCatalogRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.materialRepository = materialRepository;
        this.catalogItemRepository = catalogItemRepository;
        this.materialLogRepository = materialLogRepository;
        this.orderRepository = orderRepository;
        this.orderCatalogRepository = orderCatalogRepository;
        this.preparationOrderRepository = preparationOrderRepository;
        this.rolePrivilegesRepository = rolePrivilegesRepository;
        this.privilegesRepository = privilegesRepository;
        this.materialCatalogRepository = materialCatalogRepository; 
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

    private String generatePrepOrderId() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('order_seq')", Long.class);
        return "PODR" + String.format("%03d", nextVal);
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

    public List<Role> getOwner() {
        return roleRepository.findByIsOwner(1);
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

    
        material.setIsDelete(true);

        materialRepository.save(material);
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


    public CatalogItem updateCatalogItem(
        String catalogId,
        String title,
        String createdBy,
        String description,
        Double price,
        String materialsJson,
        MultipartFile file
    ) throws IOException {

        CatalogItem existingCatalogItem = catalogItemRepository.findById(catalogId)
                .orElseThrow(() -> new RuntimeException("Catalog Item not found: " + catalogId));
        existingCatalogItem.setTitle(title);
        existingCatalogItem.setCreatedBy(createdBy);
        existingCatalogItem.setDescription(description);
        existingCatalogItem.setPrice(price);

        if (file != null && !file.isEmpty()) {
            existingCatalogItem.setAttachment(file.getBytes());
        }

        ObjectMapper mapper = new ObjectMapper();
        List<MaterialCatalogDTO> materialsDto = Arrays.asList(
            mapper.readValue(materialsJson, MaterialCatalogDTO[].class)
        );

        List<MaterialCatalog> materialEntities = new ArrayList<>();
        for (MaterialCatalogDTO dto : materialsDto) {
            MaterialCatalog mc = new MaterialCatalog();
            mc.setCatalog(existingCatalogItem);

            Material material = materialRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Material not found: " + dto.getMaterialId()));

            mc.setMaterial(material);
            mc.setReqQty(dto.getReqQty());

            materialEntities.add(mc);
        }
        existingCatalogItem.setMaterialCatalogs(materialEntities);
        existingCatalogItem.getMaterialCatalogs().clear();

        for (MaterialCatalogDTO dto : materialsDto) {
            MaterialCatalog mc = new MaterialCatalog();
            mc.setCatalog(existingCatalogItem);

            Material material = materialRepository.findById(dto.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found: " + dto.getMaterialId()));

            mc.setMaterial(material);
            mc.setReqQty(dto.getReqQty());

            existingCatalogItem.getMaterialCatalogs().add(mc);
        }

        return catalogItemRepository.save(existingCatalogItem);
    }

    public List<CatalogItem> getAllCatalogItem() {
        return catalogItemRepository.findAll();
    }

    public Map<String, Object> getMaterialsForCatalog(String catalogId) {
        List<MaterialCatalog> materialCatalogs = materialCatalogRepository.findByCatalog_Id(catalogId);

        if (materialCatalogs.isEmpty()) {
            throw new RuntimeException("No materials found for catalog id: " + catalogId);
        }

        CatalogItem catalog = materialCatalogs.get(0).getCatalog();

        List<Map<String, Object>> materials = materialCatalogs.stream()
            .map(mc -> {
                Map<String, Object> map = new HashMap<>();
                map.put("materialId", mc.getMaterial().getId());
                map.put("materialName", mc.getMaterial().getMaterialName());
                map.put("unit", mc.getMaterial().getUnit());
                map.put("reqQty", mc.getReqQty());
                return map;
            })
            .collect(Collectors.toList());


        Map<String, Object> response = new HashMap<>();
        response.put("catalogId", catalog.getId());
        response.put("title", catalog.getTitle());
        response.put("materials", materials);

        return response;
    }


    public void deleteCatalogItem(String id) {
        CatalogItem catalogItem = catalogItemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Catalog Item not found"));

        catalogItem.setIsDelete(true);

        catalogItemRepository.save(catalogItem);
    }

    // ================ MATERIAL LOG ===============
    public MaterialLog insertMaterialLog(MaterialLog materialLog) {
        if (materialLog.getMaterial() != null && materialLog.getMaterial().getId() != null) {
            Material material = materialRepository.findById(materialLog.getMaterial().getId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            int currentStock = material.getStockQty();
            int qtyChange = materialLog.getQty();

            if ("Keluar".equalsIgnoreCase(materialLog.getType())) {
                if (currentStock < qtyChange) {
                    throw new RuntimeException("Stok material tidak mencukupi");
                }
                material.setStockQty(currentStock - qtyChange);
            } else if ("Masuk".equalsIgnoreCase(materialLog.getType())) {
                material.setStockQty(currentStock + qtyChange);
            }

            materialRepository.save(material);

            materialLog.setMaterial(material);
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

    public Map<String, Integer> getMaterialLogSummary() {
        int totalMasuk = materialLogRepository.sumQtyByType("Masuk");
        int totalKeluar = materialLogRepository.sumQtyByType("Keluar");
        int totalProduksi = preparationOrderRepository.sumTotalProduksi();

        Map<String, Integer> summary = new HashMap<>();
        summary.put("totalMasuk", totalMasuk);
        summary.put("totalKeluar", totalKeluar);
        summary.put("totalProduksi", totalProduksi);
        return summary;
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

    public Orders updateOrder(
        String orderNo,
        String deptStore,
        Date deadline,
        String status,
        String notes,
        String orderCatalogsJson,
        MultipartFile file
    ) throws IOException {

        Orders existingOrder = orderRepository.findById(orderNo)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderNo));

        existingOrder.setDeptStore(deptStore);
        existingOrder.setDeadline(deadline);
        existingOrder.setStatus(status);
        existingOrder.setNotes(notes);

        if (file != null && !file.isEmpty()) {
            existingOrder.setAttachment(file.getBytes());
        }

        ObjectMapper mapper = new ObjectMapper();
        List<OrderCatalogDTO> catalogsDto = Arrays.asList(
                mapper.readValue(orderCatalogsJson, OrderCatalogDTO[].class)
        );

        existingOrder.getOrderCatalogs().clear();

        List<OrderCatalog> orderCatalogEntities = new ArrayList<>();
        for (OrderCatalogDTO dto : catalogsDto) {
            OrderCatalog oc = new OrderCatalog();
            oc.setOrder(existingOrder);

            CatalogItem catalogItem = catalogItemRepository.findById(dto.getCatalogId())
                    .orElseThrow(() -> new RuntimeException("Catalog not found: " + dto.getCatalogId()));

            oc.setCatalogItem(catalogItem);
            oc.setQty(dto.getQty());
            orderCatalogEntities.add(oc);
        }

        existingOrder.getOrderCatalogs().addAll(orderCatalogEntities);

        return orderRepository.save(existingOrder);
    }


    public byte[] getOrderAttachment(String orderNo) {
        Orders order = orderRepository.findById(orderNo)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return order.getAttachment();
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
    
    // preparation order
    public PreparationOrder insertPreparationOrder(PreparationOrder preparationOrder) {
        preparationOrder.setId(generatePrepOrderId());
        if (preparationOrder.getOrders() != null && preparationOrder.getOrders().getOrderNo() != null) {
            Orders existingOrder = orderRepository.findById(preparationOrder.getOrders().getOrderNo())
                    .orElseThrow(() -> new RuntimeException("Order not found: " + preparationOrder.getOrders().getOrderNo()));

            preparationOrder.setOrders(existingOrder);
        } else {
            throw new RuntimeException("Order data is missing in request body");
        }
        return preparationOrderRepository.save(preparationOrder);
    }

    public PreparationOrder updatePreparationOrder(String id, PreparationOrder updatedPreparationOrder) {
        return preparationOrderRepository.findById(id).map(preparationOrder -> {
            preparationOrder.setApprovalPic(updatedPreparationOrder.getApprovalPic());
            preparationOrder.setNote(updatedPreparationOrder.getNote());
            preparationOrder.setProductionPic(updatedPreparationOrder.getProductionPic());
            preparationOrder.setStatus(updatedPreparationOrder.getStatus());
            return preparationOrderRepository.save(preparationOrder);
        }).orElse(null);
    }

    public PreparationOrder getPreparationOrder(String id) {
        return preparationOrderRepository.findById(id).orElse(null);
    }

    public List<PreparationOrder> getAllPreparationOrders() {
        return preparationOrderRepository.findAll();
    }

    public void deletePreparationOrder(String id) {
        PreparationOrder preparationOrder = preparationOrderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Preparation Order not found"));
        preparationOrderRepository.delete(preparationOrder);
    }

    public PreparationOrder updatePreparationOrderStatus(String id, String status) {
        return preparationOrderRepository.findById(id).map(preparationOrder -> {
            preparationOrder.setStatus(status);
            return preparationOrderRepository.save(preparationOrder);
        }).orElse(null);
    }

    // =============== ROLE & PRIVILEGE =============
    public Role updateRolePrivileges(String roleId, List<String> privilegeIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        List<Privileges> privileges = privilegesRepository.findAllById(privilegeIds);
        role.setPrivileges(new HashSet<>(privileges));

        return roleRepository.save(role);
    }
    
    public List<RolePrivileges> getAllRolePrivileges() {
        return rolePrivilegesRepository.findAll();
    }

    public List<RolePrivileges> getPrivilegesByRole(String roleId) {
        return rolePrivilegesRepository.findByPkRoleId(roleId);
    }
    
    // =========================== DASHBOARD ================================
    // Menghitung preparation order per bulan
    public Map<Integer, Integer> getPreparationOrderPerMonth(int year) {
        List<Object[]> results = preparationOrderRepository.countPerMonth(year);
        Map<Integer, Integer> data = new HashMap<>();
        for (Object[] row : results) {
            Integer month = ((Number) row[0]).intValue(); 
            Integer count = ((Number) row[1]).intValue();
            data.put(month, count);
        }
        return data;
    }

    // Menghitung preparation order per tahun
    public Map<Integer, Integer> getPreparationOrderPerYear() {
        int currentYear = java.time.Year.now().getValue();
        int startYear = currentYear - 4;
        List<Object[]> results = preparationOrderRepository.countPerYear(startYear);

        Map<Integer, Integer> data = new HashMap<>();
        for (Object[] row : results) {
            Integer year = ((Number) row[0]).intValue();
            Integer count = ((Number) row[1]).intValue();
            data.put(year, count);
        }
        return data;
    }

    public Map<Integer, Integer> getOrderPerMonth(int year) {
        List<Object[]> results = orderRepository.countPerMonth(year);
        Map<Integer, Integer> data = new HashMap<>();
        for (Object[] row : results) {
            Integer month = ((Number) row[0]).intValue(); 
            Integer count = ((Number) row[1]).intValue();
            data.put(month, count);
        }
        return data;
    }

    // Menghitung  order per tahun
    public Map<Integer, Integer> getOrderPerYear() {
        int currentYear = java.time.Year.now().getValue();
        int startYear = currentYear - 4;
        List<Object[]> results = orderRepository.countPerYear(startYear);

        Map<Integer, Integer> data = new HashMap<>();
        for (Object[] row : results) {
            Integer year = ((Number) row[0]).intValue();
            Integer count = ((Number) row[1]).intValue();
            data.put(year, count);
        }
        return data;
    }

}
