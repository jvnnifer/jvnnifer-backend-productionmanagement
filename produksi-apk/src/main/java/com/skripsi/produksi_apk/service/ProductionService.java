package com.skripsi.produksi_apk.service;

import com.skripsi.produksi_apk.entity.User;
import com.skripsi.produksi_apk.model.LoginRequest;
import com.skripsi.produksi_apk.repository.ProductionRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ProductionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ProductionRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ProductionService(ProductionRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String generateUserId() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('user_seq')", Long.class);
        return "USR" + String.format("%03d", nextVal);
    }

    public User registerUser(String username, String password, String role) {
        User user = new User();
        user.setId(generateUserId());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

   public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return userRepository.findByUsername(loginRequest.getUsername())
            .map(user -> {
                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Login Success");
                    response.put("data", Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "role", user.getRole()
                    ));
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                            "success", false,
                            "message", "Login failed! Username or password was wrong."
                        ));
                }
            })
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                    "success", false,
                    "message", "Login failed! Username or password was wrong."
                )));
    }

    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(String id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        }).orElse(null);
    }

}
