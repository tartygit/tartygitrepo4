package com.cth.sdm.repository;

import com.cth.sdm.entity.DocumentPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DocumentPhaseRepository extends JpaRepository<DocumentPhase, Long> {
    Optional<DocumentPhase> findByPhaseNumber(Integer phaseNumber);
}
