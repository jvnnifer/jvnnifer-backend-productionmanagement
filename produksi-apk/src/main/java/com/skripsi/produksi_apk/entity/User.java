package com.skripsi.produksi_apk.entity;

import jakarta.persistence.*;

@Entity
@Table(name="app_user")
public class User {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password; 

    @ManyToOne
    @JoinColumn(name = "role_id") 
    private Role role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    
}
