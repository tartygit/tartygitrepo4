package com.cth.sdm.repository;

import com.cth.sdm.entity.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {
    List<AuditTrail> findByUsername(String username);
}
