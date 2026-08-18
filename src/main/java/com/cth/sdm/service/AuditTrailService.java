package com.cth.sdm.service;

import com.cth.sdm.entity.AuditTrail;
import com.cth.sdm.repository.AuditTrailRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditTrailService {

    private final AuditTrailRepository auditTrailRepository;

    public AuditTrailService(AuditTrailRepository auditTrailRepository) {
        this.auditTrailRepository = auditTrailRepository;
    }

    public void logAction(String username, String action, String details) {
        AuditTrail trail = new AuditTrail(username, action, details, "127.0.0.1");
        trail.setTimestamp(LocalDateTime.now());
        auditTrailRepository.save(trail);
    }
}
