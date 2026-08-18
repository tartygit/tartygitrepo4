package com.cth.sdm.repository;

import com.cth.sdm.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByDocumentId(String documentId);
    List<Document> findByPhaseNumber(Integer phaseNumber);
    List<Document> findByStatus(String status);
    List<Document> findByMakerUsername(String makerUsername);
    long countByPhaseNumber(Integer phaseNumber);
}
