package com.skripsi.produksi_apk.service;

import com.skripsi.produksi_apk.entity.User;
import com.skripsi.produksi_apk.repository.ProductionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

   public String login(String username, String password) {
        return userRepository.findByUsername(username)
            .map(user -> {
                if (passwordEncoder.matches(password, user.getPassword())) {
                    return "true";
                } else {
                    return "false";
                }
            })
            .orElse("false");
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
