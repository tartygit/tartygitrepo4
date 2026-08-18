package com.cth.sdm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_trails")
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String action;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String details;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    public AuditTrail() {}

    public AuditTrail(String username, String action, String details, String ipAddress) {
        this.username = username;
        this.action = action;
        this.details = details;
        this.ipAddress = ipAddress;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
