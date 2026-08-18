package com.cth.sdm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    private String email;

    private String phone;

    @Column(nullable = false, length = 20)
    private String role; // ROLE_ADMIN, ROLE_MAKER, ROLE_CHECKER

    private boolean enabled = true;

    @Column(name = "account_locked")
    private boolean accountLocked = false;

    @Column(name = "failed_attempts")
    private int failedAttempts = 0;

    @Column(name = "password_expiry_date")
    private LocalDateTime passwordExpiryDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public User(String username, String password, String fullName, String email, String phone, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public boolean isAccountLocked() { return accountLocked; }
    public void setAccountLocked(boolean accountLocked) { this.accountLocked = accountLocked; }

    public int getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }

    public LocalDateTime getPasswordExpiryDate() { return passwordExpiryDate; }
    public void setPasswordExpiryDate(LocalDateTime passwordExpiryDate) { this.passwordExpiryDate = passwordExpiryDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
