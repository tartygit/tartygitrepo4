package com.cth.sdm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_id", nullable = false, unique = true, length = 50)
    private String documentId; // e.g. P101, P201, P202

    @Column(name = "document_code", nullable = false, length = 50)
    private String documentCode;

    @Column(name = "app_code", nullable = false, length = 3)
    private String appCode; // 3-character application code

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String description;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false, length = 20)
    private String fileType;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "version_number", nullable = false, length = 20)
    private String versionNumber;

    @Column(name = "phase_number", nullable = false)
    private Integer phaseNumber;

    @Column(nullable = false, length = 50)
    private String status; // PENDING_APPROVAL, APPROVED, REJECTED, PROCESSED

    @Column(name = "maker_username", nullable = false, length = 50)
    private String makerUsername;

    @Column(name = "checker_username", length = 50)
    private String checkerUsername;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Lob
    @Column(name = "ai_recommendation", columnDefinition = "CLOB")
    private String aiRecommendation;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Document() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getDocumentCode() { return documentCode; }
    public void setDocumentCode(String documentCode) { this.documentCode = documentCode; }

    public String getAppCode() { return appCode; }
    public void setAppCode(String appCode) { this.appCode = appCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getVersionNumber() { return versionNumber; }
    public void setVersionNumber(String versionNumber) { this.versionNumber = versionNumber; }

    public Integer getPhaseNumber() { return phaseNumber; }
    public void setPhaseNumber(Integer phaseNumber) { this.phaseNumber = phaseNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMakerUsername() { return makerUsername; }
    public void setMakerUsername(String makerUsername) { this.makerUsername = makerUsername; }

    public String getCheckerUsername() { return checkerUsername; }
    public void setCheckerUsername(String checkerUsername) { this.checkerUsername = checkerUsername; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getAiRecommendation() { return aiRecommendation; }
    public void setAiRecommendation(String aiRecommendation) { this.aiRecommendation = aiRecommendation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
