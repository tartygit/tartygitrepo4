package com.cth.sdm.service;

import com.cth.sdm.entity.Document;
import com.cth.sdm.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApprovalWorkflowService {

    private final DocumentRepository documentRepository;
    private final NotificationService notificationService;
    private final AuditTrailService auditTrailService;

    public ApprovalWorkflowService(DocumentRepository documentRepository,
                                  NotificationService notificationService,
                                  AuditTrailService auditTrailService) {
        this.documentRepository = documentRepository;
        this.notificationService = notificationService;
        this.auditTrailService = auditTrailService;
    }

    @Transactional
    public Document submitDocument(Document document, String makerUsername) {
        document.setStatus("PENDING_APPROVAL");
        document.setMakerUsername(makerUsername);
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        Document saved = documentRepository.save(document);

        auditTrailService.logAction(makerUsername, "SUBMIT_DOCUMENT",
                "Document " + saved.getDocumentId() + " submitted for approval.");

        notificationService.sendEmail("approvers@organization.com",
                "New Document Pending Approval: " + saved.getDocumentId(),
                "Maker " + makerUsername + " submitted document " + saved.getTitle());
        notificationService.sendSms("+1234567890",
                "SDM Alert: Document " + saved.getDocumentId() + " requires approval.");

        return saved;
    }

    @Transactional
    public Document approveDocument(Long id, String checkerUsername) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));

        doc.setStatus("APPROVED");
        doc.setCheckerUsername(checkerUsername);
        doc.setUpdatedAt(LocalDateTime.now());

        Document updated = documentRepository.save(doc);

        auditTrailService.logAction(checkerUsername, "APPROVE_DOCUMENT",
                "Document " + doc.getDocumentId() + " approved by checker " + checkerUsername);

        notificationService.sendEmail(doc.getMakerUsername() + "@organization.com",
                "Document Approved: " + doc.getDocumentId(),
                "Your document " + doc.getTitle() + " was approved by " + checkerUsername);

        return updated;
    }

    @Transactional
    public Document rejectDocument(Long id, String checkerUsername, String reason) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));

        doc.setStatus("REJECTED");
        doc.setCheckerUsername(checkerUsername);
        doc.setRejectionReason(reason);
        doc.setUpdatedAt(LocalDateTime.now());

        Document updated = documentRepository.save(doc);

        auditTrailService.logAction(checkerUsername, "REJECT_DOCUMENT",
                "Document " + doc.getDocumentId() + " rejected by " + checkerUsername + ". Reason: " + reason);

        notificationService.sendEmail(doc.getMakerUsername() + "@organization.com",
                "Document Rejected: " + doc.getDocumentId(),
                "Your document " + doc.getTitle() + " was rejected. Reason: " + reason);

        return updated;
    }

    public List<Document> getPendingApprovals() {
        return documentRepository.findByStatus("PENDING_APPROVAL");
    }
}
