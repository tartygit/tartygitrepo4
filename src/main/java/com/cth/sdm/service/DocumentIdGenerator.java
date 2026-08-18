package com.cth.sdm.service;

import com.cth.sdm.repository.DocumentRepository;
import org.springframework.stereotype.Service;

@Service
public class DocumentIdGenerator {

    private final DocumentRepository documentRepository;

    public DocumentIdGenerator(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Generates sequential document ID per phase:
     * Phase 1 -> P101, P102...
     * Phase 2 -> P201, P202...
     * Phase 7 -> P701, P702...
     */
    public synchronized String generateNextDocumentId(int phaseNumber) {
        long count = documentRepository.countByPhaseNumber(phaseNumber);
        long nextSeq = count + 1;
        return String.format("P%d%02d", phaseNumber, nextSeq);
    }
}
